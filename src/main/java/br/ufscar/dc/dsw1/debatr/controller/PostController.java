package br.ufscar.dc.dsw1.debatr.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.domain.Comment;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.ICommentService;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Controller
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;

    @GetMapping("/")
    public String home(Model model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User user = userService.buscarPorUsername(userDetails.getUsername());
        List<Post> posts = postService.getUserTimeline(user);
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/compose")
    public String getPosts(Post post, Model model) {
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
    public String createPost(
            @Valid Post post,
            BindingResult results,
            @RequestParam("forum_id") Long forumId, @RequestParam("topic_id") Long topicId, Model model) {
        
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if(results.hasErrors()) {
            if (details != null) {
                User user = userService.buscarPorUsername(details.getUsername());
    
                if (user != null) {
                    model.addAttribute("forum_count", user.getForuns().size());
                    model.addAttribute("foruns", user.getForuns());
                }
            }
            return "compose";
        }
        model.addAttribute("errored", true);
        if (details != null) {
            User currentUser = userService.buscarPorUsername(details.getUsername());

            if (currentUser != null) {
                Post newPost = new Post(post.getTitle(), post.getContent(), currentUser, null, null);
                postService.save(newPost, forumId, topicId);

                model.addAttribute("errored", false);
            }
        }
        return "redirect:compose";
    }

    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable("postId") long postId, ModelMap model) {
        Post post = postService.findById(postId);
        Forum forum = post.getForum();
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User currentUser = userService.buscarPorUsername(details.getUsername());

        if(forum.getAccessScope() == 2){
            if(currentUser == null || (currentUser.getAr() == 0 && !forum.isMember(currentUser.getUsername()))){
                return "redirect:/";
            }
        }
        model.addAttribute("comments", commentService.buscarPorPost(post));
        model.addAttribute("post", post);

        return "postDetail";
    } 
    

    
    @PostMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") long postId, ModelMap model) {

        Post post = postService.findById(postId);
        postService.delete(post);

        return "redirect:/";
    }

    @GetMapping("/post/{postId}/comment")  
    public String createComment(@PathVariable("postId") long postId, @RequestParam("content") String content, Model model) {
        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();
        User currentUser = userService.buscarPorUsername(details.getUsername());
        Post post = postService.findById(postId);
        List<Comment> comments = commentService.buscarPorPost(post);
        Comment comment = new Comment(post, currentUser, content);
        commentService.save(comment);
        comments.add(0, comment);
        model.addAttribute("comments", comments);
        return "fragments/comments";
    }
}
