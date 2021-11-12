package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post extends AbstractEntity {
    public Post() {}

    public Post(String title, String content, User author, Forum forum, Topic topic) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.forum = forum;
        this.topic = topic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 256)
    private String title;

    @NotNull
    @NotBlank
    @Length(min = 1)
    private String content;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    
    @OneToMany
    private List<Reaction> reactions;

    @OneToMany
    private List<Comment> comments;

    @ManyToOne
    private Forum forum;

    @ManyToOne()
    private Topic topic;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
