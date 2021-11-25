package br.ufscar.dc.dsw1.debatr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw1.debatr.dao.IForumDAO;
import br.ufscar.dc.dsw1.debatr.dao.IPostDAO;
import br.ufscar.dc.dsw1.debatr.dao.IUserDAO;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;

@SpringBootApplication
public class DebatrApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebatrApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(IUserDAO usuarioDAO, IForumDAO forumDAO, IPostDAO postDAO, BCryptPasswordEncoder encoder) {
		return (args) -> {

			User u1 = new User();
			u1.setUsername("username");
			u1.setPassword(encoder.encode("password"));
			u1.setDisplayName("user");
			u1.setEmail("user@user.com"); 
			usuarioDAO.save(u1);

			Forum f1 = new Forum(u1, 1, 1, "forum1", "descrição");
			forumDAO.save(f1);

			Post p1 = new Post("Título", "Conteúdo", u1, f1, null);
			postDAO.save(p1);
		};
	}

}
