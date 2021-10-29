package br.ufscar.dc.dsw1.debatr.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

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
    public String salvar(
            HttpServletRequest request,
            @RequestParam("email") String email,
            @RequestParam("display_name") String displayName,
            @RequestParam("username") String username,
            @RequestParam("password") String plaintextPassword
    ) {
        User newUser = new User(displayName, username, email, encoder.encode(plaintextPassword));
        service.salvar(newUser);

        try {
            request.login(username, plaintextPassword);
        } catch (ServletException e) {
            return "redirect:login";
        }

        return "redirect:home";
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
                    model.addAttribute("isProfileOwner",
                            currentUser.getId() == profileOwner.getId());
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
    public ModelAndView editProfile(
            @RequestParam("display-name") String displayName,
            @RequestParam("biography") String biography,
            ModelMap model
    ) {
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
    public ModelAndView deleteAccount(
            @RequestParam("password") String password,
            ModelMap model,
            HttpServletRequest request
            ) {
        User currentUser = getCurrentUser();

        if (currentUser != null) {
            if (doPasswordsMatch(password, currentUser.getPassword())) {
                service.excluir(currentUser.getId());

                try {
                    request.logout();
                } catch (ServletException ignored) {}
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
}