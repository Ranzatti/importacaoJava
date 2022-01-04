package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ChequePK implements Serializable {

    private Integer fichaConta;
    private Integer numero;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String historico;

    public ChequePK() {
    }

    public ChequePK(Integer fichaConta, Integer numero, Date data, String historico) {
        this.fichaConta = fichaConta;
        this.numero = numero;
        this.data = data;
        this.historico = historico;
    }

    public Integer getFichaConta() {
        return fichaConta;
    }

    public void setFichaConta(Integer fichaConta) {
        this.fichaConta = fichaConta;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
