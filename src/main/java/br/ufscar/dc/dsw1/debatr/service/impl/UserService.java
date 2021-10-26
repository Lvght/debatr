package br.ufscar.dc.dsw1.debatr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw1.debatr.dao.IUserDAO;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Service
@Transactional(readOnly = false)
public class UserService implements IUserService {

    @Autowired
    IUserDAO dao;

    public void salvar(User usuario) {
        dao.save(usuario);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User buscarPorId(Long id) {
        return dao.findById(id.longValue());
    }

    @Transactional(readOnly = true)
    public List<User> buscarTodos() {
        return dao.findAll();
    }
}