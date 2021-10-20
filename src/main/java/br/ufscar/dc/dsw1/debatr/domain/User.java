package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "debatr_user")
public class User extends AbstractEntity {
    @Id
    private int id;

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

    @NotEmpty
    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Column(name = "profile_image")
    private String profileImageUrl;

    private String description;

    private double reputation;

    private int ar;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplay_name() {
        return displayName;
    }

    public int getId() {
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
}