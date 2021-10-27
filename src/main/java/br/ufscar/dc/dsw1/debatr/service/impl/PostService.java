package br.ufscar.dc.dsw1.debatr.service.impl;

import br.ufscar.dc.dsw1.debatr.dao.IPostDAO;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class PostService implements IPostService {
    @Autowired
    private IPostDAO dao;

    public void save(Post post) {
        dao.save(post);
    }

    public void delete(Post post) {
        dao.delete(post);
    }

    public void getUserTimeline(User user) {
        dao.findPostsByForum_Members(user);
    }


}
