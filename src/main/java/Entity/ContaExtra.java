package Entity;

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

    public ContaExtra() {
        this.id = new ContaExtraPK();
    }

    public ContaExtra(Date ano, Integer contaExtra, String nome, Integer empresa) {
        this.id = new ContaExtraPK(ano, contaExtra);
        this.nome = nome;
        this.empresa = empresa;
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

}
