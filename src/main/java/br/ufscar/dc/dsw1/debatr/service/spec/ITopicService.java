package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;
import br.ufscar.dc.dsw1.debatr.domain.Topic;

public interface ITopicService {
    Topic buscarPorId(Long id);

    List<Topic> buscarTodosPorForum(Long forumId);

    Topic salvar(Topic topic);

    void excluir(Topic topic);
}