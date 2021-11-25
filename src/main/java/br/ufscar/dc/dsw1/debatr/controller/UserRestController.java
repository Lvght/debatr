package br.ufscar.dc.dsw1.debatr.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;

@RestController
@RequestMapping("/api")
public class UserRestController implements IBaseRestController<User> {
    @Autowired
    private IUserService service;

    @Override
    public User parse(JSONObject json) {
        String displayName = (String) json.get("display_name");
        String username = (String) json.get("username");
        String email = (String) json.get("email");
        String password = (String) json.get("password");


        return new User(displayName, username, email, password);
    }

    // Lista todos os usuários.
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    // Permite criar um novo usuário.
    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody JSONObject json) {
        User parsedUser = this.parse(json);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(parsedUser));
    }

    // Permite buscar um usuário pelo seu ID.
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody JSONObject json) {
        User parsedUser = this.parse(json);
        parsedUser.setId(id);
        return ResponseEntity.ok(service.salvar(parsedUser));
    }

    // Permite deletar um usuário pelo seu ID.
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        User toExcludeUser = service.buscarPorId(id);
        if (toExcludeUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        service.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
