package br.ufscar.dc.dsw1.debatr.service.spec;


import java.util.List;

import br.ufscar.dc.dsw1.debatr.domain.Comment;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface ICommentService {
    Comment findById(long id);
    
    List<Comment> findAll();

    List<Comment> buscarPorPost(Post post);
    
    Comment save(Comment comment);

    void delete(Comment comment);
}
