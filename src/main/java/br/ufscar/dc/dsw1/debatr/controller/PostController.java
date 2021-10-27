package br.ufscar.dc.dsw1.debatr.controller;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.AuthenticatedUserHelper;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @GetMapping("/compose")
    public String getPosts(Model model) {
        UserDetails userDetails = AuthenticatedUserHelper.getCurrentAuthenticatedUser();

        if (userDetails != null) {
            User user = userService.buscarPorUsername(userDetails.getUsername());

            if (user != null) {
                model.addAttribute("forum_count", user.getForuns().size());
            }
        }

        return "compose";
    }
}
