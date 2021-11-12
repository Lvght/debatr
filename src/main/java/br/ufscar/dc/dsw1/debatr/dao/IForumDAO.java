package br.ufscar.dc.dsw1.debatr.dao;

import java.util.List;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw1.debatr.domain.Forum;

@SuppressWarnings("unchecked")
public interface IForumDAO extends CrudRepository<Forum, Long> {

	Forum findById(long id);

	List<Forum> findAll();

	Forum save(Forum forum);

	Forum findByTitle(String title); 

	void deleteById(Long id);

	// @Query("SELECT f.* from Forum f JOIN usuario_ingressa_forum u ON f.id_forum =
	// u.id_forum WHERE id_usuario = :id_usuario")
	// public Comment getUserForuns(@Param("id_postagem") Long id_usuario);

	// @Query("SELECT * from usuario_ingressa_forum WHERE id_usuario = :id_usuario
	// AND id_forum = :id_forum")
	// public Comment usuarioIngressa(@Param("id_postagem") Long id_usuario,
	// @Param("id_forum") Long id_forum);

	// @Query("INSERT INTO usuario_ingressa_forum (id_usuario, id_forum) VALUES
	// (:id_usuario, :id_forum)")
	// public Comment ingressar(@Param("id_postagem") Long id_usuario,
	// @Param("id_forum") Long id_forum);

	// @Query("DELETE FROM usuario_ingressa_forum WHERE id_usuario = :id_usuario AND
	// id_forum = :id_forum")
	// public Comment sair(@Param("id_postagem") Long id_usuario, @Param("id_forum")
	// Long id_forum);

	// @Query("SELECT COUNT(*) AS rowcount FROM usuario_ingressa_forum WHERE
	// id_forum = :id_forum")
	// public Comment contarMembros(@Param("id_forum") Long id_forum);

}