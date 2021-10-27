package br.ufscar.dc.dsw1.debatr.helper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AuthenticatedUserHelper {
    public static UserDetails getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        return null;
    }
}
