package br.ufscar.dc.dsw1.debatr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw1.debatr.dao.IForumDAO;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;

@Service
@Transactional(readOnly = false)
public class ForumService implements IForumService {

    @Autowired
    IForumDAO dao;

    public void salvar(Forum forum) {
        dao.save(forum);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Forum buscarPorId(Long id, User user) {
        Forum forum = dao.findById(id.longValue());
        // forum.setUserIngress(forum.getMembers().contains(user));
        return forum;
    }

    @Transactional(readOnly = true)
    public List<Forum> buscarTodos(User user) {
        List<Forum> foruns = dao.findAll();
        for (Forum forum : foruns) {
            // forum.setUserIngress(forum.getMembers().contains(user));
        }
        return foruns;
    }
}