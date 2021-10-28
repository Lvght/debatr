package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IForumService {
    Forum buscarPorId(Long id, User user);

    List<Forum> buscarTodos(User user);

    void salvar(Forum forum);

    void excluir(Long id);
}