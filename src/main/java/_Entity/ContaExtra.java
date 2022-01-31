package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPCONTAEXTRA")
public class ContaExtra implements Serializable {

    @EmbeddedId
    private ContaExtraPK id;
    private String nome;
    private Integer empresa;
    private Date anoRestos;
    private String tipoResto;

    public ContaExtra() {
        this.id = new ContaExtraPK();
    }

    public ContaExtra(Date ano, Integer contaExtra, String nome, Integer empresa, Date anoRestos, String tipoResto) {
        this.id = new ContaExtraPK(ano, contaExtra);
        this.nome = nome;
        this.empresa = empresa;
        this.anoRestos = anoRestos;
        this.tipoResto = tipoResto;
    }

    public ContaExtraPK getId() {
        return id;
    }

    public void setId(ContaExtraPK id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Date getAnoRestos() {
        return anoRestos;
    }

    public void setAnoRestos(Date anoRestos) {
        this.anoRestos = anoRestos;
    }

    public String getTipoResto() {
        return tipoResto;
    }

    public void setTipoResto(String tipoResto) {
        this.tipoResto = tipoResto;
    }
}
