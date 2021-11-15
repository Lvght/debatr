package br.ufscar.dc.dsw1.debatr.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.dom4j.tree.AbstractEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post extends AbstractEntity {
    public Post() {
    }

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
    @Column(length = 65535)
    private String content;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
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

    private int getReactionCountOfType(int reactionType) {
        int count = 0;

        for (Reaction reaction : reactions) {
            if (reaction.getType() == reactionType) {
                count++;
            }
        }
        return count;
    }

    public int getLikes() {
        return getReactionCountOfType(1);
    }

    public int getDeslikes() {
        return getReactionCountOfType(0);
    }

    public int userReacted(String username) {
        for (Reaction reaction : reactions) {
            if (reaction.author.getUsername().equals(username)) {
                return reaction.getType();
            }
        }

        return -1;
    }

    public String toString() {
        return "Post{" + "id=" + id + ", title='" + title + '\'' + ", content='" + content + '\'' + ", author=" + author
                + ", createdAt=" + createdAt + ", reactions=" + reactions + ", comments=" + comments + ", forum="
                + forum + ", topic=" + topic + '}';
    }
}
