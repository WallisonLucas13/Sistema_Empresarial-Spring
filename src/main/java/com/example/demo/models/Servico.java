package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long identificador;

    @Column
    @NotBlank
    private String nomeServico;

    @Column
    @NotBlank
    private String desServico;

    @Column
    private LocalDate dateService;

    @Column
    private String custoServicoFinal = "R$ 0";

    @Column
    private String totalMateriais = "R$ 0";

    @Column
    private String maoDeObra = "R$ 0";

    @OneToMany(cascade = CascadeType.ALL)
    private List<Material> materiais;

        public String getNomeServico() {
            return nomeServico;
        }

        public void setNomeServico(String nomeServico) {
            this.nomeServico = nomeServico;
        }

        public Long getId() {
            return identificador;
        }

        public String getDesServico() {
            return desServico;
        }

        public void setDesServico(String desServico) {
            this.desServico = desServico;
        }

        public List<Material> getMateriais() {
            return materiais;
        }

        public void setMateriais(List<Material> materiais) {
            this.materiais = materiais;
        }

        public LocalDate getDateService() {
        return dateService;
    }

        public void setDateService(LocalDate dateService) {
        this.dateService = dateService;
    }

        public Long getIdentificador() {
        return identificador;
    }

        public String getCustoServicoFinal() {
        return custoServicoFinal;
    }

        public void setCustoServicoFinal(String custoServicoFinal) {
        this.custoServicoFinal = custoServicoFinal;
    }

        public String getTotalMateriais() {
        return totalMateriais;
    }

        public void setTotalMateriais(String totalMateriais) {
        this.totalMateriais = totalMateriais;
    }

        public String getMaoDeObra() {
        return maoDeObra;
    }

        public void setMaoDeObra(String maoDeObra) {
        this.maoDeObra = maoDeObra;
    }
}

