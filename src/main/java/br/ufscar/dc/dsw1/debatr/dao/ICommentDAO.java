// package br.ufscar.dc.dsw1.debatr.dao;

// import java.util.List;

// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.CrudRepository;
// import org.springframework.data.repository.query.Param;

// import br.ufscar.dc.dsw1.debatr.domain.Comment;

// @SuppressWarnings("unchecked")
// public interface ICommentDAO extends CrudRepository<Comment, Long> {

// Comment findById(long id);

// List<Comment> findAll();

// Comment save(Comment comment);

// void deleteById(Long id);

// @Query("SELECT * FROM comentario WHERE id_postagem = :id_postagem")
// public List<Comment> getAlllCommentsFromPost(@Param("id_postagem") Long
// id_postagem);

// }