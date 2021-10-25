package br.ufscar.dc.dsw1.debatr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw1.debatr.domain.Post;

@SuppressWarnings("unchecked")
public interface IPostDAO extends CrudRepository<Post, Long> {

	// Post findById(long id);

	// List<Post> findAll();

	// Post save(Post post);

	// void deleteById(Long id);

	// @Query("WHERE id_forum = :id_forum AND id_topico = :id_topico
	// get_forum_posts_1 get_forum_posts_2")
	// public List<Post> getForumPosts(@Param("id_forum") Long id_forum,
	// @Param("id_topico") Long id_topico);

	// @Query("SELECT COUNT(*) from postagem WHERE id_forum = :id_forum")
	// public Long countForumPosts(@Param("id_forum") Long id_forum);
}