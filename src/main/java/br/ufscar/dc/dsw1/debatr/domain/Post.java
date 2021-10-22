package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    private String title;

    private String content;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    Date createdAt;
    
    @OneToMany
    List<Reaction> reactions;

    @OneToMany
    List<Comment> comments;
}