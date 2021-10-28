package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IUserService {
    User buscarPorId(Long id);

    User buscarPorUsername(String username);

    List<User> buscarTodos();

    void salvar(User user);

    void excluir(Long id);
}