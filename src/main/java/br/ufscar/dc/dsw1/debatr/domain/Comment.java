package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Comment extends AbstractEntity {
    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    Post post;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;

    @NotNull
    @NotBlank
    @Size(min = 1)
    @Column(length = 65535)
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Comment() {

    }

    public Comment(Post post, User author, @NotNull @NotBlank @Size(min = 1, max = 255) String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }
}
