package br.ufscar.dc.dsw1.debatr.service.spec;

import br.ufscar.dc.dsw1.debatr.domain.Post;
import br.ufscar.dc.dsw1.debatr.domain.User;

public interface IPostService {
    void save(Post post);
    void save(Post post, long forumId);
    void getUserTimeline(User user);
    void delete(Post post);
}
