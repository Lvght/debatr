package br.ufscar.dc.dsw1.debatr.controller;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.ufscar.dc.dsw1.debatr.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

import java.io.UnsupportedEncodingException;

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
    public String cadastrar(User usuario) {
        return "register";
    }

    @PostMapping("/register")
    public String salvar(HttpServletRequest request, @RequestParam("email") String email,
                         @RequestParam("display_name") String displayName, @RequestParam("username") String username,
                         @RequestParam("password") String plaintextPassword) {
        User newUser = new User(displayName, username, email, encoder.encode(plaintextPassword));
        service.salvar(newUser);

        EmailService emailService = new EmailService();
        emailService.sendVerifyYourEmail(newUser, getBaseUrl(request));

        try {
            request.login(username, plaintextPassword);
        } catch (ServletException e) {
            return "redirect:login";
        }

        return "redirect:/config/verify-email";
    }

    @GetMapping("/login")
    public String getLoginForm() {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        if (details != null) {
            return "/";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request
    ) {
        User user = service.buscarPorUsername(username);

        if (user != null) {
            if (encoder.matches(password, user.getPassword())) {
                try {
                    request.login(username, password);
                } catch (ServletException e) {
                    e.printStackTrace();
                }

                return "redirect:/config/verify-email";

            }
        }

        return "login";
    }

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
    public String profile(@PathVariable("username") String username, Model model) {

        User profileOwner = service.buscarPorUsername(username);

        if (profileOwner != null) {
            model.addAttribute("profileOwner", profileOwner);

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
    public String emailConfirmation() {
        EmailService emailService = new EmailService();

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());

            if (user != null && user.getEmailVerifiedAt() != null) {
                return "redirect:/";
            }
        }

        return "confirmEmail";

//        try {
//            emailService.send(
//                    new InternetAddress("no-reply@debatr.com", "Equipe Debatr"),
//                    new InternetAddress("viniciusluz@estudante.ufscar.br", "Vinicius"),
//                    "Caixa Econômica Federal",
//                    "Socorro");
//            System.out.println("Deu certoooooooooooooooooooooo");
//        } catch (UnsupportedEncodingException ignored) {
//            System.out.println("Deu erradoooooooooooooo");
//        }
    }
}