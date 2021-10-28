package br.ufscar.dc.dsw1.debatr.service.impl;

import br.ufscar.dc.dsw1.debatr.dao.IForumDAO;
import br.ufscar.dc.dsw1.debatr.dao.IPostDAO;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
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

    public void save(Post post) {
        postDAO.save(post);
    }

    public void save(Post post, long forumId) {
        Forum forum = forumDAO.findById(forumId);
        post.setForum(forum);

        postDAO.save(post);
    }

    public void delete(Post post) {
        postDAO.delete(post);
    }

    public List<Post> getUserTimeline(User user) {
        return postDAO.findPostsByForum_Members(user);
    }
}
