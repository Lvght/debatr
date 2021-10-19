package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "User")
public class User extends AbstractEntity<Long> {

    @NotBlank(message = "{NotBlank.user.username}")
    @Size(max = 15)
    @Column(nullable = false, length = 15)
    private String username;

    @NotBlank(message = "{NotBlank.user.titulo}")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String titulo;

    @NotBlank(message = "{NotBlank.user.autor}")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String autor;
    
    @NotNull(message = "{NotNull.user.ano}")
    @Column(nullable = false, length = 5)
    private Integer ano;
    
    @NotNull(message = "{NotNull.user.preco}")
    @Column(nullable = false, columnDefinition = "DECIMAL(8,2) DEFAULT 0.0")
    private BigDecimal preco;
    
    @NotNull(message = "{NotNull.user.editora}")
    @ManyToOne
    @JoinColumn(name = "editora_id")
    private Editora editora;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }
}