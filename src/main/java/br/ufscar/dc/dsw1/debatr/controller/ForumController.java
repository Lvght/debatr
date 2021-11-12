package br.ufscar.dc.dsw1.debatr.controller;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.ITopicService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private IForumService forumService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITopicService topicService;

    @GetMapping("/register")
    public String cadastrar(Forum forum) {
        return "createForum";
    }

    @PostMapping("/cadastrar")
    public String salvar(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "iconFile", required = false) MultipartFile iconFile,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "accessScope", defaultValue = "1") int accessScope,
            @RequestParam(value = "postScope", defaultValue = "1") int postScope
    ) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());

            if (user != null) {
                Forum newForum = new Forum(user, postScope, accessScope, title, description);
                forumService.salvarEAdicionarMembro(newForum, user, iconFile);

                return "redirect:/forum/" + newForum.getId();
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
        model.addAttribute("currentUser", user);
        model.addAttribute("foruns", foruns);
        model.addAttribute("status", "ingressar");
        return "/listForuns";
    }

    @GetMapping("/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        model.addAttribute("currentUser", user);
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

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        if(forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + id;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("forum", forum);
        return "/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(            
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "accessScope", defaultValue = "1") int accessScope,
            @RequestParam(value = "postScope", defaultValue = "1") int postScope,
            @PathVariable(value = "id") Long id
        ) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());
            Forum forum = forumService.buscarPorId(id, user);
            forum.setTitle(title);
            forum.setDescription(description);
            forum.setAccessScope(accessScope);
            forum.setPostScope(postScope);
            forumService.salvar(forum);
        }
        
        return "redirect:/forum/" + id;
    }


    @GetMapping("/{id}/topic")
    public String topicForm(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        if(forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + id;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("forum", forum);
        return "/createTopic";
    }

    @PostMapping("/{id}/topic")
    public String topicCreation(@Valid @ModelAttribute("topic") Topic topic,
                                BindingResult result,
                                ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(user.getId(), user);
        if(forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + forum.getId();
        }
        topic.setForum(forum);
        topicService.salvar(topic);
        
        return "redirect:/forum/" + forum.getId();
    }
}