package br.ufscar.dc.dsw1.debatr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw1.debatr.dao.ITopicDAO;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.service.spec.ITopicService;

@Service
@Transactional(readOnly = false)
public class TopicService implements ITopicService {

    @Autowired
    ITopicDAO topicDAO;

    public Topic buscarPorId(Long id){
        return topicDAO.findById(id.longValue());
    }

    public List<Topic> buscarTodosPorForum(Long forumId){
        return topicDAO.findTopicsByForumId(forumId);
    }

    public Topic salvar(Topic topic){
        return topicDAO.save(topic);
    }

    public void excluir(Topic topic){
        topicDAO.delete(topic);
    }
}