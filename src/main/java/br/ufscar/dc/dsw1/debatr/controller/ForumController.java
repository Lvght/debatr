package br.ufscar.dc.dsw1.debatr.controller;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

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
    public String salvar(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "accessScope", defaultValue = "1") int accessScope,
            @RequestParam(value = "postScope", defaultValue = "1") int postScope
    ) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());

            if (user != null) {
                Forum newForum = new Forum(user, postScope, accessScope, title, description);
                forumService.salvar(newForum);

                return "redirect:/home";
            }
        }

        return "redirect:/forum/register";
    }

    @GetMapping("/list")
    public String list(Model model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        List<Forum> foruns = forumService.buscarTodos(user);
        foruns.sort(new Comparator<Forum>() {
            public int compare(Forum f1, Forum f2) {
                if (f1.getId() > f2.getId())
                    return 1;
                if (f1.getId() < f2.getId())
                    return -2;
                return 0;
            }
        });
        model.addAttribute("foruns", foruns);
        model.addAttribute("status", "ingressar");
        return "/listForuns";
    }

    @GetMapping("/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        model.addAttribute("posts", forum.getPosts());
        model.addAttribute("forum", forumService.buscarPorId(id, user));
        model.addAttribute("status", "ingressar");
        return "/forum";
    }

    @GetMapping("/signIn/{id_forum}")
    public String signIn(@PathVariable("id_forum") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        user.getForuns().add(forum);
        userService.salvar(user);
        // forum.setUserIngress(true);
        model.addAttribute("forum", forum);
        return "fragments/signButton";
    }

    @GetMapping("/signOut/{id_forum}")
    public String signOut(@PathVariable("id_forum") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        user.getForuns().remove(forum);
        userService.salvar(user);
        // forum.setUserIngress(false);
        model.addAttribute("forum", forum);
        return "fragments/signButton";
    }
}