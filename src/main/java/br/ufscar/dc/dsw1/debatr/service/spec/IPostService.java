package br.ufscar.dc.dsw1.debatr.service.spec;

import java.util.List;

import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IPostService {
    void save(Post post);

    void save(Post post, long forumId);

    List<Post> getUserTimeline(User user);

    void delete(Post post);
}
