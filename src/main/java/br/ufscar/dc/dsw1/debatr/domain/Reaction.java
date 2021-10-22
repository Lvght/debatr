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
}
