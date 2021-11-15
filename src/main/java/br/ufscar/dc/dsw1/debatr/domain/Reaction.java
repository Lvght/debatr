package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Reaction extends AbstractEntity {
    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(0)
    @Max(1)
    private int type;

    @ManyToOne
    User author;

    @ManyToOne
    Post post;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
