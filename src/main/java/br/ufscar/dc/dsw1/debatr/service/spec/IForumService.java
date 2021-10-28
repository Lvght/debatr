package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;
import br.ufscar.dc.dsw1.debatr.domain.Forum;

public interface IForumService {
    Forum buscarPorId(Long id);

    List<Forum> buscarTodos();

    void salvar(Forum editora);

    void excluir(Long id);
}