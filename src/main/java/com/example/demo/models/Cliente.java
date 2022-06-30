package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column
    @NotBlank
    private String nome;

    @Column
    @NotBlank
    private String telefone;

    @Column
    @NotBlank
    private String bairro;

    @Column
    @NotBlank
    private String endereco;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Servico> servicos;

    @Column
    private LocalDate dataCadastro;

        public Long getId() {
        return id;
    }

        public void setId(Long id) {
        this.id = id;
    }

        public String getNome() {
        return nome;
    }

        public void setNome(String nome) {
        this.nome = nome;
    }

        public String getTelefone() {
        return telefone;
    }

        public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

        public String getBairro() {
        return bairro;
    }

        public void setBairro(String bairro) {
        this.bairro = bairro;
    }

        public String getEndereco() {
        return endereco;
    }

        public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

        public LocalDate getDataCadastro() {
        return dataCadastro;
    }

        public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

        public List<Servico> getServicos() {
        return servicos;
    }

        public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}
