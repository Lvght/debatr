package br.ufscar.dc.dsw1.debatr.controller;

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

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private IForumService service;

    @GetMapping("/register")
    public String cadastrar(Forum forum) {
        return "createForum";
    }

    @PostMapping("/cadastrar")
    public String salvar(@Valid Forum forum, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "redirect:/forum/register";
        }

        service.salvar(forum);
        attr.addFlashAttribute("sucess", "Fórum inserido com sucesso.");
        return "redirect:home";
    }
}