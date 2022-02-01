package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPCONTACXPLANOC")
public class ContasCXPlanoC implements Serializable {

    @EmbeddedId
    private ContasCXPlanoCPK id;
    private Integer banco;
    private String agencia;
    private String conta;
    private Integer fichaPC;
    private Integer fichaPCAplic;

    public ContasCXPlanoC() {
        this.id = new ContasCXPlanoCPK();
    }

    public ContasCXPlanoC(Date ano, Integer fichaConta, Integer banco, String agencia, String conta, Integer fichaPC, Integer fichaPCAplic) {
        this.id = new ContasCXPlanoCPK(ano, fichaConta);
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.fichaPC = fichaPC;
        this.fichaPCAplic = fichaPCAplic;
    }

    public ContasCXPlanoCPK getId() {
        return id;
    }

    public void setId(ContasCXPlanoCPK id) {
        this.id = id;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Integer getFichaPC() {
        return fichaPC;
    }

    public void setFichaPC(Integer fichaPC) {
        this.fichaPC = fichaPC;
    }

    public Integer getFichaPCAplic() {
        return fichaPCAplic;
    }

    public void setFichaPCAplic(Integer fichaPCAplic) {
        this.fichaPCAplic = fichaPCAplic;
    }
}
