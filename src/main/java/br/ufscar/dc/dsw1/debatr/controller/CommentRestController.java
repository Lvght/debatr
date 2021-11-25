package br.ufscar.dc.dsw1.debatr.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.ufscar.dc.dsw1.debatr.domain.Comment;
import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.service.spec.ICommentService;
import br.ufscar.dc.dsw1.debatr.service.spec.IUserService;
import br.ufscar.dc.dsw1.debatr.service.spec.IPostService;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@RestController

public class CommentRestController {

	@Autowired
	private ICommentService serviceComment;
	@Autowired
	private IPostService    servicePost;
	@Autowired
	private IUserService    serviceUser;
		
	private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}

	
	@SuppressWarnings("unchecked")
	private void parse(Comment comment, JSONObject json) {
		
		Map<String, Object> map = (Map<String, Object>) json.get("comment");

		Object id = map.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				comment.setId(((Integer) id).longValue());
			} else {
				comment.setId((Long) id);
			}
		}
		
		comment.setPost(servicePost.findById(Long.valueOf((Integer) map.get("post_id"))));
        comment.setAuthor(serviceUser.buscarPorId(Long.valueOf((Integer) map.get("Author_id"))));
        comment.setContent((String) map.get("content"));
    
	}

	@GetMapping(path = "/comment")
	public ResponseEntity<List<Comment>> lista() {
		List<Comment> lista = serviceComment.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}

	@GetMapping(path = "/comment/{id}")
	public ResponseEntity<Comment> comentario(@PathVariable("id") long id) {
		Comment comment = serviceComment.findById(id);
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(comment);
	}

	@GetMapping(path = "/comment/post/{id}")
	public ResponseEntity<List<Comment>> listaPorPost(@PathVariable("id") long id) {
		
		Post post = servicePost.findById(id);
		List<Comment> lista = serviceComment.buscarPorPost(post);
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping(path = "/comment")
	@ResponseBody
	public ResponseEntity<Comment> cria(@RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Comment comment = new Comment();
				parse(comment, json);
                
                if(comment.getAuthor() == null ){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

				serviceComment.save(comment);
				return ResponseEntity.ok(comment);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

	@PutMapping(path = "/comment/{id}")
	public ResponseEntity<Comment> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Comment comment = serviceComment.findById(id);
				if (comment == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(comment, json);
					comment = serviceComment.save(comment);
					return ResponseEntity.ok(comment);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

	@DeleteMapping(path = "/comment/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Comment comment = serviceComment.findById(id);
		if (comment == null) {
			return ResponseEntity.notFound().build();
		} else {
			serviceComment.delete(comment);
			return ResponseEntity.noContent().build();
		}
	}
}