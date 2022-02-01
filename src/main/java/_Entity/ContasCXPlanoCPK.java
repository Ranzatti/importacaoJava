package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class ContasCXPlanoCPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private int fichaConta;

    public ContasCXPlanoCPK() {
    }

    public ContasCXPlanoCPK(Date ano, int fichaConta) {
        this.ano = ano;
        this.fichaConta = fichaConta;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public int getFichaConta() {
        return fichaConta;
    }

    public void setFichaConta(int fichaConta) {
        this.fichaConta = fichaConta;
    }
}
