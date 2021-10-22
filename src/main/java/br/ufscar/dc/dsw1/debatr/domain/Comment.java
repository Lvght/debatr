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

    @ManyToOne
    User author;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;
}
