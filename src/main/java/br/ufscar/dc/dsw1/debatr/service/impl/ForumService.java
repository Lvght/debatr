package br.ufscar.dc.dsw1.debatr.service.impl;

import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw1.debatr.dao.IUserDAO;
import br.ufscar.dc.dsw1.debatr.dao.ITopicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw1.debatr.dao.IForumDAO;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.ExternalImageServiceHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;

@Service
@Transactional(readOnly = false)
public class ForumService implements IForumService {

    @Autowired
    IForumDAO dao;

    @Autowired
    IUserDAO userDAO;

    @Autowired
    ITopicDAO topicDAO;

    public Forum salvar(Forum forum) {
        return dao.save(forum);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    public Forum buscarPorTitulo(String titulo){
        return dao.findByTitle(titulo);
    }


    @Transactional(readOnly = true)
    public Forum buscarPorId(Long id, User user) {
        Forum forum = dao.findById(id.longValue());
        List<Topic> topics = topicDAO.findTopicsByForum(forum);
        forum.setTopics(topics);
        return forum;
    }

    @Transactional(readOnly = true)
    public List<Forum> buscarTodos(User user) {
        List<Forum> foruns = dao.findAll();
        for (Forum forum : foruns) {
            // forum.setUserIngress(forum.getMembers().contains(user));
        }
        return foruns;
    }

    @Transactional()
    @Override
    public void salvarEAdicionarMembro(Forum forum, User member) {
        Forum f = dao.save(forum);
        member.getForuns().add(f);
        userDAO.save(member);
    }

    @Transactional()
    @Override
    public void salvarEAdicionarMembro(Forum forum, User member, MultipartFile iconFile) {
        Forum f = dao.save(forum);
        member.getForuns().add(f);
        userDAO.save(member);

        if (iconFile != null) {
            // obtém a extensão de profileImage
            final String fileExtension = iconFile.getOriginalFilename()
                    .substring(iconFile.getOriginalFilename().lastIndexOf(".") + 1);

            ExternalImageServiceHelper helper = new ExternalImageServiceHelper();
            helper.uploadImage(iconFile, "profile/" + f.getId() + "." + fileExtension);

            final String imageUri = "https://debatr-sb-media.s3.sa-east-1.amazonaws.com/" + "profile/" + f.getId() + "." + fileExtension;
            f.setIconImageUrl(imageUri);

            dao.save(f);
        }
    }
}