package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "debatr_user")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractEntity {

    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @NotNull
    @NotBlank
    @Size(min = 1, max = 25)
    @Column(name = "display_name", nullable = false, length = 25)
    private String displayName;

    @NotBlank
    @NotEmpty
    @Length(min = 3)
    @NotNull
    @Column(unique = true, length = 15)
    private String username;

    @Column(name = "email_verified_at")
    private Date emailVerifiedAt;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @NotEmpty
    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @NotEmpty
    @NotNull
    @Column(unique = false, nullable = false, length = 60)
    private String password;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @ManyToMany()
    List<Forum> foruns;

    private String description;

    private double reputation;

    private int ar;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public User() {}

    public User(String displayName, String username, String email, String password) {
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Date getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Date emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public List<Forum> getForuns() {
        return foruns;
    }

    public void setForuns(List<Forum> foruns) {
        this.foruns = foruns;
    }

    public String getDisplay_name() {
        return displayName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public double getReputation() {
        return reputation;
    }

    public int getAr() {
        return ar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}