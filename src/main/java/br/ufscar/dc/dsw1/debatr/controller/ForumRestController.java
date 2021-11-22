package br.ufscar.dc.dsw1.debatr.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;


@RestController
public class ForumRestController {

	@Autowired
	private IForumService forumService;

    @Autowired
	private IUserService userService;

	private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private void parse(Forum forum, JSONObject json) {

		Map<String, Object> map = (Map<String, Object>) json.get("forum");

		Object id = map.get("id");
        if(id != null){
            if (id instanceof Integer) {
                forum.setId(((Integer) id).longValue());
            } else {
                forum.setId(((Long) id));
            }
        }

        forum.setOwner(userService.buscarPorId(Long.valueOf((Integer) map.get("user_id"))));
        forum.setPostScope((Integer) map.get("post_scope"));
        forum.setAccessScope((Integer) map.get("access_scope"));
        forum.setTitle((String) map.get("title"));
        forum.setDescription((String) map.get("description"));
        forum.setIconImageUrl((String) map.get("icon_image_url"));
	}

	@GetMapping(path = "/forum")
	public ResponseEntity<List<Forum>> lista() {
		List<Forum> lista = forumService.buscarTodos();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}

	@GetMapping(path = "/forum/{id}")
	public ResponseEntity<Forum> lista(@PathVariable("id") long id) {
		Forum forum = forumService.buscarPorId(id);
		if (forum == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(forum);
	}

    @DeleteMapping(path = "/forum/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Forum forum = forumService.buscarPorId(id);
		if (forum == null) {
			return ResponseEntity.notFound().build();
		} else {
			forumService.excluir(id);
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping(path = "/forum")
	@ResponseBody
	public ResponseEntity<Forum> cria(@RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Forum forum = new Forum();
				parse(forum, json);

                Boolean titleAlreadyUsed = forumService.buscarPorTitulo(forum.getTitle()) != null;

                if(forum.getOwner() == null || titleAlreadyUsed){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

				forum = forumService.salvar(forum);
				return ResponseEntity.ok(forum);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

	@PutMapping(path = "/forum/{id}")
	public ResponseEntity<Forum> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Forum forum = forumService.buscarPorId(id);

				if (forum == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(forum, json);

                    Boolean titleAlreadyUsed = forumService.buscarPorTitulo(forum.getTitle()) != null;
                
                    if(forum.getOwner() == null || titleAlreadyUsed){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }

					forumService.salvar(forum);
					return ResponseEntity.ok(forum);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
}

