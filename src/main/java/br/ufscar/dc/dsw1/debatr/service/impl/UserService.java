package br.ufscar.dc.dsw1.debatr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw1.debatr.dao.IUserDAO;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.ExternalImageServiceHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@Service
@Transactional(readOnly = false)
public class UserService implements IUserService {

    @Autowired
    IUserDAO dao;

    public void salvar(User usuario) {
        dao.save(usuario);
    }

    @Transactional
    public void saveAndSetProfileImage(User usuario, MultipartFile profileImage) {
        dao.save(usuario);

        if (profileImage != null) {
            // obtém a extensão de profileImage
            final String fileExtension = profileImage.getOriginalFilename()
                    .substring(profileImage.getOriginalFilename().lastIndexOf(".") + 1);

            ExternalImageServiceHelper helper = new ExternalImageServiceHelper();
            helper.uploadImage(profileImage, "profile/" + usuario.getId() + "." + fileExtension);

            final String imageUri = "https://debatr-sb-media.s3.sa-east-1.amazonaws.com/" + "profile/" + usuario.getId() + "." + fileExtension;
            usuario.setProfileImageUrl(imageUri);

            dao.save(usuario);
        }

    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User buscarPorId(Long id) {
        return dao.findById(id.longValue());
    }

    @Transactional(readOnly = true)
    public User buscarPorUsername(String username) {
        User u = dao.findByUsername(username);

        // if (u != null && u.getProfileImageUrl() != null) {
        //     // TODO implementar aqui a recuperação com url autoassinada.
        //     u.setProfileImageUrl("/profile/" + u.getId() + ".jpg");
        // }

        return u;
    }

    @Transactional(readOnly = true)
    public List<User> buscarTodos() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public User buscarPorEmail(String email) {
        return dao.findByEmail(email);
    }
}