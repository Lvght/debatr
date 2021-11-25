package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;

import br.ufscar.dc.dsw1.debatr.domain.Forum;
import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.Topic;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IPostService {
    Post findById(long id);

    Post save(Post post);

    Post save(Post post, long forumId, long topicId);

    List<Post> getAll();

    List<Post> getForumPosts(Forum forum, Topic topic);

    List<Post> getUserTimeline(User user);

    void delete(Post post);
}
