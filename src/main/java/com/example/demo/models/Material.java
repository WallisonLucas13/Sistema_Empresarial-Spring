package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Material {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column
    @NotEmpty
    private String nomeMaterial;

    @Column
    @NotEmpty
    private String quantMaterial;

    @Column
    @NotEmpty
    private String valorMaterial;

    @Column
    private String custoTotal;

    public Long getId() {
        return id;
    }

    public String getNomeMaterial() {
        return nomeMaterial;
    }

    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }

    public String getQuantMaterial() {
        return quantMaterial;
    }

    public void setQuantMaterial(String quantMaterial) {
        this.quantMaterial = quantMaterial;
    }

    public String getValorMaterial() {
        return valorMaterial;
    }

    public void setValorMaterial(String valorMaterial) {
        this.valorMaterial = valorMaterial;
    }

    public String getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(String custoTotal) {
        this.custoTotal = custoTotal;
    }
}
