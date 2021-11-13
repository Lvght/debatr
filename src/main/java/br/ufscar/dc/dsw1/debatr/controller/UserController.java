package br.ufscar.dc.dsw1.debatr.controller;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.helper.JwtHelper;
import br.ufscar.dc.dsw1.debatr.service.impl.EmailService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }

    private User getCurrentUser() {
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            return service.buscarPorUsername(details.getUsername());
        }

        return null;
    }

    private boolean doPasswordsMatch(String plaintextPassoword, String userPassword) {
        return encoder.matches(plaintextPassoword, userPassword);
    }

    @GetMapping("/register")
    public String cadastrar(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String salvar(HttpServletRequest request,
            @Valid User user,
            BindingResult result,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage){

            User userSameUsername = service.buscarPorUsername(user.getUsername());
            User userSameEmail = service.buscarPorEmail(user.getEmail());
                
        if (result.hasErrors() || userSameUsername != null || userSameEmail != null) {
            if(userSameEmail != null){
                result.rejectValue("email", "", "Email já utilizado");
            }
            if(userSameUsername != null){
                result.rejectValue("username", "", "Username já utilizado");
            }
            return "register";
        }


        User newUser = new User(user.getDisplayName(), user.getUsername(), user.getEmail(), encoder.encode(user.getPassword()));
        service.saveAndSetProfileImage(newUser, profileImage);

        EmailService emailService = new EmailService();
        emailService.sendVerifyYourEmail(newUser, getBaseUrl(request));

        try {
            request.login(user.getUsername(), user.getPassword());
        } catch (ServletException e) {
            return "redirect:login";
        }

        return "redirect:/config/verify-email";
    }

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest request, ModelMap model) {

        model.addAttribute("forgotPasswordPath", getBaseUrl(request) + "/config/password-reset");
        model.addAttribute("user", new User()); 

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        if (details != null) {
            return "/";
        }

        return "login";
    }

    // @PostMapping("/login")
    // public String login(@Valid User user,  BindingResult result, HttpServletRequest request, ModelMap model) {
    //     User loginUser = service.buscarPorUsername(user.getUsername());
    //     System.out.println(result.getAllErrors());
    //     if (loginUser != null) {
    //         if (encoder.matches(user.getPassword(), loginUser.getPassword())) {
    //             try {
    //                 request.login(user.getUsername(), loginUser.getPassword());
    //             } catch (ServletException e) {
    //                 e.printStackTrace();
    //             }

    //             return "redirect:/config/verify-email";

    //         }
    //     }
    //     System.out.println(result.getAllErrors());

    //     return "login";
    // }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, HttpServletRequest request, ModelMap model) {

        User profileOwner = service.buscarPorUsername(username);

        if (profileOwner != null) {
            model.addAttribute("profileOwner", profileOwner);

            // TODO Existe maneira eficiente de obter a url a partir do Thymeleaf?
            model.addAttribute("resendVerifyEmailPath", getBaseUrl(request) + "/config/resend-verify-token");

            UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

            if (details != null) {
                User currentUser = service.buscarPorUsername(details.getUsername());

                if (currentUser != null) {
                    model.addAttribute("isProfileOwner", currentUser.getId() == profileOwner.getId());
                }
            }
        } else {
            model.addAttribute("404", true);
        }

        return "profile";
    }

    @GetMapping("/config/profile")
    public String getEditProfileForm(Model model) {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());
            model.addAttribute("user", user);
        }

        return "editProfile";
    }

    @PostMapping("/config/profile")
    public ModelAndView editProfile(@RequestParam("display-name") String displayName,
            @RequestParam("biography") String biography, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        if (userDetails != null) {
            User currentUser = service.buscarPorUsername(userDetails.getUsername());

            if (currentUser != null) {
                currentUser.setDisplayName(displayName);
                currentUser.setDescription(biography);

                service.salvar(currentUser);
            }
        }

        // Indicates that the current profile has been successfully updated.
        model.addAttribute("updated", true);
        return new ModelAndView("redirect:profile", model);
    }

    @GetMapping("/config/delete-account")
    public String deleteAccountConfirmation(Model model) {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User currentUser = service.buscarPorUsername(details.getUsername());

            if (currentUser != null) {
                if (currentUser.getAdministeredForuns().size() != 0) {
                    model.addAttribute("foruns", currentUser.getAdministeredForuns());
                    return "cannotDeleteAccount";
                }
            }
        }

        return "confirmAccountDeletion";
    }

    @PostMapping("/config/delete-account")
    public ModelAndView deleteAccount(@RequestParam("password") String password, ModelMap model,
            HttpServletRequest request) {
        User currentUser = getCurrentUser();

        if (currentUser != null) {
            if (doPasswordsMatch(password, currentUser.getPassword())) {
                service.excluir(currentUser.getId());

                try {
                    request.logout();
                } catch (ServletException ignored) {
                }
            } else {
                model.addAttribute("wrong_password", true);
                model.addAttribute("pw", request.getAttribute("password"));

                if (currentUser.getAdministeredForuns().size() == 0) {
                    return new ModelAndView("redirect:/config/delete-account", model);
                } else {
                    return new ModelAndView("redirect:/config/delete-account", model);

                }
            }
        }

        return new ModelAndView("redirect:goodbye", model);
    }

    @GetMapping("/config/verify-email")
    public String emailConfirmation(HttpServletRequest request, ModelMap model) {
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());

            if (user != null && user.getEmailVerifiedAt() != null) {
                return "redirect:/";
            }
        }

        model.addAttribute("homePath", getBaseUrl(request));

        return "confirmEmail";
    }

    /**
     * Permite que o usuário verifique seu email.
     */
    @GetMapping("/config/verify-email/{token}")
    public String verifyEmail(@PathVariable("token") String token, HttpServletRequest request, ModelMap model) {

        if (JwtHelper.isTokenValid(token)) {
            DecodedJWT jwt = JwtHelper.getDecodedJWT(token);

            String username = jwt.getClaim("username").asString();
            User newlyRegisteredUser = service.buscarPorUsername(username);

            newlyRegisteredUser.setEmailVerifiedAt(new Date());
            service.salvar(newlyRegisteredUser);

            model.addAttribute("verified", true);
        }

        return "emailVerified";
    }

    @GetMapping("/config/resend-verify-token")
    public String resendVerifyToken(HttpServletRequest request, ModelMap model) {
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());

            if (user != null && user.getEmailVerifiedAt() == null) {
                EmailService emailService = new EmailService();
                emailService.sendVerifyYourEmail(user, getBaseUrl(request));
            }
        }

        model.addAttribute("homePath", getBaseUrl(request));
        return "confirmEmail";
    }

    /**
     * Envia o usuário para o formulário de escolher uma nova senha.
     */
    @GetMapping("/config/password-reset/{token}")
    public String forgotPasswordForm(@PathVariable("token") String token, HttpServletRequest request, ModelMap model) {
        model.addAttribute("token", token);
        return "alterPassword";
    }

    /**
     * Processa a alteração de senha do usuário.
     */
    @PostMapping("/config/change-password")
    public String changePassword(
        @RequestParam("token") String token,
        @RequestParam("password") String password
    ) {
        DecodedJWT jwt = JwtHelper.getDecodedJWT(token);

        if (jwt != null && JwtHelper.isTokenValid(token)) {
            String username = jwt.getClaim("username").asString();
            User user = service.buscarPorUsername(username);

            System.out.println("Changing password for user [" + username + "].");

            if (user != null) {
                user.setPassword(encoder.encode(password));
                service.salvar(user);
            }
        }

        return "redirect:/";
    }

    /**
     * Envia o usuário para a página de redefinição de senha.
     */
    @GetMapping("/config/password-reset")
    public String forgotPassword(HttpServletRequest request, ModelMap model) {
        model.addAttribute("passwordResetPath", getBaseUrl(request) + "/config/password-reset");
        return "forgotPassword";
    }

    /**
     * Processa o pedido de redefinição de senha, enviando o email com token para o usuário.
     */
    @PostMapping("/config/password-reset")
    public String forgotPassword(@RequestParam("email") String email, HttpServletRequest request, ModelMap model) {
        User user = service.buscarPorEmail(email);

        if (user != null) {
            EmailService emailService = new EmailService();
            emailService.sendPasswordResetEmail(user, getBaseUrl(request));
        }

        model.addAttribute("sent", true);
        return "checkYourEmailPasswordChange";
    }
}
