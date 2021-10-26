package br.ufscar.dc.dsw1.debatr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.ufscar.dc.dsw1.debatr.dao.IUserDAO;
import br.ufscar.dc.dsw1.debatr.domain.User;

public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserDAO dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = dao.findByUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new UsuarioDetails(usuario);
    }
}
