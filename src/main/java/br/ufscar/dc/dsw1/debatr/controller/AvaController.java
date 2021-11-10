package br.ufscar.dc.dsw1.debatr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.helper.Ava2Helper;
import br.ufscar.dc.dsw1.debatr.service.impl.UserService;

@Controller
public class AvaController {
    @Autowired
    private UserService service;

    @GetMapping("/config/ufscar/verify")
    public String getVerifyForm() {
        return "verifyAvaAccount";
    }

    @PostMapping("/config/ufscar/verify")
    public String verify(@RequestParam("ar") String ar, @RequestParam("password") String password) {

        UserDetails details = AuthenticatedUserHelper.getCurrentAuthenticatedUserDetails();

        if (details != null) {
            User user = service.buscarPorUsername(details.getUsername());

            if (user != null) {
                if (Ava2Helper.verifyUserCredentials(ar, password)) {
                    user.setAr(Integer.parseInt(ar));
                    service.salvar(user);
                }
            }
        }

        return "redirect:/";
    }
}
