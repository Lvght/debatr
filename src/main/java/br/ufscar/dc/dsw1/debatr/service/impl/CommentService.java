package br.ufscar.dc.dsw1.debatr.service.impl;

import br.ufscar.dc.dsw1.debatr.dao.ICommentDAO;
import br.ufscar.dc.dsw1.debatr.domain.Comment;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.service.spec.ICommentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class CommentService implements ICommentService {
    @Autowired
    private ICommentDAO commentDAO;

    public Comment findById(long id) {
        return commentDAO.findById(id);
    }

    public void save(Comment comment) {
        commentDAO.save(comment);
    }

    public void delete(Comment comment) {
        commentDAO.delete(comment);
    }

    public List<Comment> buscarPorPost(Post post){
        return commentDAO.findByPost(post);
    }

}
