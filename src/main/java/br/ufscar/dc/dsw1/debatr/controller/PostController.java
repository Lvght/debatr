package br.ufscar.dc.dsw1.debatr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public String home(Model model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        List<Post> posts = postService.getUserTimeline(user);
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/compose")
    public String getPosts(Model model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());

            if (user != null) {
                model.addAttribute("forum_count", user.getForuns().size());
                model.addAttribute("foruns", user.getForuns());
            }
        }

        return "compose";
    }

    @PostMapping("/compose")
    public String createPost(@RequestParam("title") String title, @RequestParam("content") String content,
            @RequestParam("forum_id") Long forumId, @RequestParam("topic_id") Long topicId, Model model) {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        model.addAttribute("errored", true);
        if (details != null) {
            User currentUser = userService.buscarPorUsername(details.getUsername());

            if (currentUser != null) {
                Post newPost = new Post(title, content, currentUser, null, null);
                postService.save(newPost, forumId, topicId);

                model.addAttribute("errored", false);
            }
        }
        return "redirect:compose";
    }

    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable("postId") long postId, ModelMap model) {

        Post post = postService.findById(postId);
        model.addAttribute("post", post);

        return "postDetail";
    } 
    

    
    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") long postId, ModelMap model) {

        Post post = postService.findById(postId);
        postService.delete(post);

        return "redirect:/";
    }
}
