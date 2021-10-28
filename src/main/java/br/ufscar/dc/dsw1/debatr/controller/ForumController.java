package br.ufscar.dc.dsw1.debatr.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.userdetails.UserDetails;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.domain.User;
import org.springframework.ui.Model;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.service.impl.ForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private IForumService forumService;

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String cadastrar(Forum forum) {
        return "createForum";
    }

    @PostMapping("/cadastrar")
    public String salvar(@Valid Forum forum, BindingResult result, RedirectAttributes attr) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (result.hasErrors()) {
            return "redirect:/forum/register";
        }

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());
            forum.setOwner(user);
            forumService.salvar(forum);
            attr.addFlashAttribute("sucess", "Fórum inserido com sucesso.");
            return "redirect:/home";
        } else {
            return "redirect:/forum/register";
        }
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Forum> foruns = forumService.buscarTodos();
        model.addAttribute("foruns", foruns);
        return "/listForuns";
    }
}