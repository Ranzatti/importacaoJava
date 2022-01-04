package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class DescontosOPPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer op;
    private Integer parcela;
    private String tipoGuia;
    private Integer guiaReceita;

    public DescontosOPPK() {
    }

    public DescontosOPPK(Date ano, Integer op, Integer parcela, String tipoGuia, Integer guiaReceita) {
        this.ano = ano;
        this.op = op;
        this.parcela = parcela;
        this.tipoGuia = tipoGuia;
        this.guiaReceita = guiaReceita;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getOp() {
        return op;
    }

    public void setOp(Integer op) {
        this.op = op;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public void setTipoGuia(String tipoGuia) {
        this.tipoGuia = tipoGuia;
    }

    public Integer getGuiaReceita() {
        return guiaReceita;
    }

    public void setGuiaReceita(Integer guiaReceita) {
        this.guiaReceita = guiaReceita;
    }
}
