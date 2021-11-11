package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IUserService {
    User buscarPorId(Long id);

    User buscarPorUsername(String username);

    User buscarPorEmail(String email);

    List<User> buscarTodos();

    void salvar(User user);

    void saveAndSetProfileImage(User user, MultipartFile file);

    void excluir(Long id);
}