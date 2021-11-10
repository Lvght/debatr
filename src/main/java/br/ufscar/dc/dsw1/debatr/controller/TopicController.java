package br.ufscar.dc.dsw1.debatr.controller;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.service.spec.IForumService;
import br.ufscar.dc.dsw1.debatr.service.spec.ITopicService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private ITopicService topicService;

    @GetMapping("")
    public String getAll(@RequestParam(value = "forumId", required = true) Long forumId, Model model) {
        List<Topic> topics = topicService.buscarTodosPorForum(forumId);
        model.addAttribute("topics", topics);
        return "fragments/topics";
    }
}