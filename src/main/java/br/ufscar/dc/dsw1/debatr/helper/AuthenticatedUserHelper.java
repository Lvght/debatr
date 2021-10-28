package br.ufscar.dc.dsw1.debatr.helper;

import br.ufscar.dc.dsw1.debatr.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AuthenticatedUserHelper {
    public static UserDetails getCurrentAuthenticatedUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        return null;
    }
}
