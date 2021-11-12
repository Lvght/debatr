package br.ufscar.dc.dsw1.debatr.domain;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.dom4j.tree.AbstractEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "forum")
public class Forum extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;

    @ManyToMany(mappedBy = "foruns", cascade = {CascadeType.DETACH})
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "forum", cascade = {CascadeType.REMOVE})
    private List<Post> posts;

    @OneToMany(mappedBy = "forum", cascade = {CascadeType.REMOVE})
    private List<Topic> topics;

    @NotNull
    @Column(name = "post_scope", nullable = false)
    private int postScope;

    @NotNull
    @Column(name = "access_scope", nullable = false)
    private int accessScope;

    @NotBlank
    @NotEmpty
    @NotNull
    @Length(max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String title;

    @NotBlank
    @NotEmpty
    @NotNull
    @Length(max = 255)
    @Column(length = 255, nullable = false)
    private String description;

    @URL
    @Column(nullable = true)
    private String iconImageUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Forum() {
    }

    public Forum(User owner, int postScope, int accessScope, String title, String description) {
        this.owner = owner;
        this.postScope = postScope;
        this.accessScope = accessScope;
        this.title = title;
        this.description = description;
    }

    /**
     * @return [true], if the user is a member or is the owner of this forum. [false], otherwise.
     */
    public boolean isMember(String username) {
        return this.owner.getUsername().equals(username) || this.members.stream()
                .filter(member -> username.equals(member.getUsername()))
                .findFirst().orElse(null) != null;
    }

    public boolean isOwner(String username) {
        return this.owner.getUsername().equals(username);
    }

    public void addMember(User newMember) {
        members.add(newMember);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getPostScope() {
        return postScope;
    }

    public void setPostScope(int postScope) {
        this.postScope = postScope;
    }

    public int getAccessScope() {
        return accessScope;
    }

    public void setAccessScope(int accessScope) {
        this.accessScope = accessScope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String icon) {
        this.iconImageUrl = icon;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

}