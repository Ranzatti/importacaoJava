package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPELEMDESPESA")
public class ElemDespesa implements Serializable {

    @EmbeddedId
    private ElemDespesaPK id;
    private Integer empresa;
    private String orgao;
    private String unidade;
    private String subUnidade;
    private String funcao;
    private String subFuncao;
    private String programa;
    private String projAtiv;
    private String categoria;
    private String grupo;
    private String modalidade;
    private String elemento;
    private String desdobramento;
    private BigDecimal orcado;

    public ElemDespesa() {
        this.id = new ElemDespesaPK();
    }

    public ElemDespesa(Date ano, Integer empresa, Integer ficha, String orgao, String unidade, String subUnidade, String funcao, String subFuncao, String programa, String projAtiv, String categoria, String grupo, String modalidade, String elemento, String desdobramento, BigDecimal orcado) {
        this.id = new ElemDespesaPK(ano, ficha);
        this.empresa = empresa;
        this.orgao = orgao;
        this.unidade = unidade;
        this.subUnidade = subUnidade;
        this.funcao = funcao;
        this.subFuncao = subFuncao;
        this.programa = programa;
        this.projAtiv = projAtiv;
        this.categoria = categoria;
        this.grupo = grupo;
        this.modalidade = modalidade;
        this.elemento = elemento;
        this.desdobramento = desdobramento;
        this.orcado = orcado;
    }

    public ElemDespesaPK getId() {
        return id;
    }

    public void setId(ElemDespesaPK id) {
        this.id = id;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getOrgao() {
        return orgao;
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getSubUnidade() {
        return subUnidade;
    }

    public void setSubUnidade(String subUnidade) {
        this.subUnidade = subUnidade;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getSubFuncao() {
        return subFuncao;
    }

    public void setSubFuncao(String subFuncao) {
        this.subFuncao = subFuncao;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getProjAtiv() {
        return projAtiv;
    }

    public void setProjAtiv(String projAtiv) {
        this.projAtiv = projAtiv;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getDesdobramento() {
        return desdobramento;
    }

    public void setDesdobramento(String desdobramento) {
        this.desdobramento = desdobramento;
    }

    public BigDecimal getOrcado() {
        return orcado;
    }

    public void setOrcado(BigDecimal orcado) {
        this.orcado = orcado;
    }

}
