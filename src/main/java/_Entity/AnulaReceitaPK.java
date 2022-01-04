package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AnulaReceitaPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private String tipo;
    private Integer anulacao;

    public AnulaReceitaPK() {
    }

    public AnulaReceitaPK(Date ano, String tipo, Integer anulacao) {
        this.ano = ano;
        this.tipo = tipo;
        this.anulacao = anulacao;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getAnulacao() {
        return anulacao;
    }

    public void setAnulacao(Integer anulacao) {
        this.anulacao = anulacao;
    }
}
