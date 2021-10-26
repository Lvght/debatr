package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IUserService {
    User buscarPorId(Long id);

    List<User> buscarTodos();

    void salvar(User editora);

    void excluir(Long id);
}