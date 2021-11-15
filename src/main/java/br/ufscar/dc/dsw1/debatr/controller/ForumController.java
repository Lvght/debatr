package br.ufscar.dc.dsw1.debatr.controller;

import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.ITopicService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private IForumService forumService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITopicService topicService;

    @Autowired
    private IPostService postService;

    @GetMapping("/register")
    public String cadastrar(Forum forum) {
        return "createForum";
    }

    @PostMapping("/cadastrar")
    public String salvar(
            @Valid Forum forum,
            BindingResult result,
            @RequestParam(value = "iconFile", required = false) MultipartFile iconFile) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if(result.hasErrors() || forumService.buscarPorTitulo(forum.getTitle()) != null) {
            result.rejectValue("title", "", "Um fórum com este título já existe");
            return "createForum";
        }

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());

            if (user != null) {
                Forum newForum = new Forum(user, forum.getPostScope(), forum.getAccessScope(), forum.getTitle(), forum.getDescription());
                forumService.salvarEAdicionarMembro(newForum, user, iconFile);

                return "redirect:/forum/" + newForum.getId();
            }
        }

        return "createForum";
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
        return "listForuns";
    }

    @GetMapping("/{id}")
    public String preEditar(@PathVariable("id") Long id, 
            @RequestParam(value = "topic_id", required = false) Long topicId, 
            ModelMap model
        ) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        Topic topic = topicId != null ? topicService.buscarPorId(topicId) : null;
        if(forum == null) {
            return "redirect:/forum/list";
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("posts", postService.getForumPosts(forum, topic));
        model.addAttribute("currentTopic", topic);
        model.addAttribute("forum", forum);
        model.addAttribute("status", "ingressar");
        return "forum";
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

    @GetMapping("/delete/{idForum}")
    public String delete(@PathVariable("idForum") long idForum) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(idForum, user);

        if (forum.getOwner().equals(user)) {
            user.getForuns().remove(forum);
            userService.salvar(user);
            forumService.excluir(idForum);
        }

        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(id, user);
        if (forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + id;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("forum", forum);
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(       
            @Valid Forum forum,
            BindingResult result,
            @RequestParam(value = "iconFile", required = false) MultipartFile iconFile, @PathVariable(value = "id") Long id) {
            UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        
        if (result.hasErrors()) {
            return "edit";
        }

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());
            Forum editForum = forumService.buscarPorId(id, user);
            editForum.setTitle(forum.getTitle());
            editForum.setDescription(forum.getDescription());
            editForum.setAccessScope(forum.getAccessScope());
            editForum.setPostScope(forum.getPostScope());
            forumService.salvar(editForum);
        }

        return "redirect:/forum/" + id;
    }

    @GetMapping("/{id}/topic")
    public String topicForm(@PathVariable("id") Long id, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());

        if(user == null) {
            return "redirect:/";
        }

        Forum forum = forumService.buscarPorId(id, user);
        if (forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + id;
        }
        model.addAttribute("currentUser", user);
        model.addAttribute("forum", forum);
        return "createTopic";
    }

    @PostMapping("/{id}/topic")
    public String topicCreation(@Valid @ModelAttribute("topic") Topic topic, BindingResult result, ModelMap model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        Forum forum = forumService.buscarPorId(user.getId(), user);
        if (forum.getOwner().getId() != user.getId()) {
            return "redirect:/forum/" + forum.getId();
        }
        topic.setForum(forum);
        topicService.salvar(topic);

        return "redirect:/forum/" + forum.getId();
    }
}