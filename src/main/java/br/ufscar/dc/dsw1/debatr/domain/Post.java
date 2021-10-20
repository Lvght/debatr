package br.ufscar.dc.dsw1.debatr.domain;

import org.dom4j.tree.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "post")
public class Post extends AbstractEntity {
    @Id
    private long id;

    private String title;

    private String content;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id", updatable = false)
    private User author;
}
