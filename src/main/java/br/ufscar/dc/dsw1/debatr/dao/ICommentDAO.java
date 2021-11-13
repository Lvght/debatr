package br.ufscar.dc.dsw1.debatr.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw1.debatr.domain.Comment;
import br.ufscar.dc.dsw1.debatr.domain.Post;

@SuppressWarnings("unchecked")
public interface ICommentDAO extends CrudRepository<Comment, Long> {

Comment findById(long id);

List<Comment> findAll();

Comment save(Comment comment);

void deleteById(Long id);

List<Comment> findByPost(Post post);
}