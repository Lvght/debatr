package br.ufscar.dc.dsw1.debatr.controller;

import javax.validation.Valid;

import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String cadastrar(User usuario) {
        return "register";
    }

    // @GetMapping("/listar")
    // public String listar(ModelMap model) {
    // model.addAttribute("usuarios", service.buscarTodos());
    // return "usuario/lista";
    // }

    @PostMapping("/user")
    public String salvar(@Valid User user, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "redirect:register";
        }

        System.out.println("password = " + user.getPassword());

        user.setPassword(encoder.encode(user.getPassword()));
        service.salvar(user);
        attr.addFlashAttribute("sucess", "Usuário inserido com sucesso.");
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
    public String editProfile(Model model) {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());
            model.addAttribute("user", user);
        }

        return "editProfile";
    }

    // @GetMapping("/editar/{id}")
    // public String preEditar(@PathVariable("id") Long id, ModelMap model) {
    // model.addAttribute("usuario", service.buscarPorId(id));
    // return "usuario/cadastro";
    // }

    // @PostMapping("/editar")
    // public String editar(@Valid User usuario, BindingResult result,
    // RedirectAttributes attr) {

    // if (result.hasErrors()) {
    // return "usuario/cadastro";
    // }

    // System.out.println(usuario.getPassword());

    // service.salvar(usuario);
    // attr.addFlashAttribute("sucess", "Usuário editado com sucesso.");
    // return "redirect:/usuarios/listar";
    // }

    // @GetMapping("/excluir/{id}")
    // public String excluir(@PathVariable("id") Long id, ModelMap model) {
    // service.excluir(id);
    // model.addAttribute("sucess", "Usuário excluído com sucesso.");
    // return listar(model);
    // }
}