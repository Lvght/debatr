package br.ufscar.dc.dsw1.debatr.service.impl;

import br.ufscar.dc.dsw1.debatr.dao.IForumDAO;
import br.ufscar.dc.dsw1.debatr.dao.IPostDAO;
import br.ufscar.dc.dsw1.debatr.dao.ITopicDAO;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class PostService implements IPostService {
    @Autowired
    private IPostDAO postDAO;

    @Autowired
    private IForumDAO forumDAO;

    @Autowired
    private ITopicDAO topicDAO;

    public Post findById(long id) {
        return postDAO.findById(id);
    }

    public void save(Post post) {
        postDAO.save(post);
    }

    public void save(Post post, long forumId, long topicId) {
        Forum forum = forumDAO.findById(forumId);
        post.setForum(forum);
        if(topicId != 0){
            Topic topic = topicDAO.findById(topicId);
            post.setTopic(topic);
        }

        postDAO.save(post);
    }

    public void delete(Post post) {
        postDAO.delete(post);
    }

    public List<Post> getForumPosts(Forum forum, Topic topic){
        if(topic != null){
            return postDAO.findByForumAndTopic(forum, topic);
        }
        return postDAO.findByForum(forum);
    }

    public List<Post> getUserTimeline(User user) {
        return postDAO.findPostsByForum_Members(user);
    }
}
