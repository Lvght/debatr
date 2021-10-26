package br.ufscar.dc.dsw1.debatr.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw1.debatr.domain.User;

@SuppressWarnings("unchecked")
public interface IUserDAO extends CrudRepository<User, Long> {

	User findById(long id);

	List<User> findAll();

	User save(User user);

	void deleteById(Long id);

	User findByUsername(String username);
}