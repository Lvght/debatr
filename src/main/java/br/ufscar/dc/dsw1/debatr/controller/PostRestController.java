package br.ufscar.dc.dsw1.debatr.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.ITopicService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@RestController
@RequestMapping("/api")
public class PostRestController implements IBaseRestController<Post> {
    @Autowired
    private IPostService service;

    @Autowired
    private IUserService userService;

    @Autowired
    private IForumService forumService;

    @Autowired
    private ITopicService topicService;

    @Override
    public Post parse(JSONObject json) {
        String title = (String) json.get("title");
        String content = (String) json.get("content");
        Integer topicId = (Integer) json.get("topic_id");
        Integer userId = (Integer) json.get("user_id");
        Integer forumId = (Integer) json.get("forum_id");

        Forum postForum = forumService.buscarPorId(forumId.longValue());
        Topic topic = topicService.buscarPorId(topicId.longValue());
        User author = userService.buscarPorId(userId.longValue());

        Post retVal = new Post(title, content, author, postForum, topic);
        System.out.println("Post [parsed]: " + retVal.toString());   

        return retVal;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Post> createPost(@RequestBody JSONObject json) {
        try {
            Post newPost = service.save(this.parse(json));

            if (newPost != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = service.getAll();
        return ResponseEntity.ok(posts);
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        Post post = service.findById(id);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(post);
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Post> deletePost(@PathVariable("id") Long id) {
        Post post = service.findById(id);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        service.delete(post);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
