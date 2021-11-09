package br.ufscar.dc.dsw1.debatr.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw1.debatr.domain.Topic;

@SuppressWarnings("unchecked")
public interface ITopicDAO extends CrudRepository<Topic, Long> {

	Topic findById(long id);

	List<Topic> findAll();

	List<Topic> findTopicsByForumId(Long forumId);

	Topic save(Topic topico);

	void deleteById(Long id);
}