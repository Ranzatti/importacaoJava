package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPCRDANL")
public class CreditoDotacao implements Serializable {

    @EmbeddedId
    private CreditoDotacaoPK id;

    @Temporal(TemporalType.DATE)
    private Date dataCredito;
    private String tipo;
    private String historico;
    private String natureza;
    private String usaOrcamento;
    private Integer lei;

    @Temporal(TemporalType.DATE)
    private Date dataLei;
    private Integer transposicao;
    private Integer transposicaoFonte;
    private Integer lancamento;

    public CreditoDotacao() {
        this.id = new CreditoDotacaoPK();
    }

    public CreditoDotacao(Date ano, Integer codigo, Date dataCredito, String tipo, String historico, String natureza, String usaOrcamento, Integer lei, Date dataLei, Integer transposicao, Integer transposicaoFonte) {
        this.id = new CreditoDotacaoPK(ano, codigo);
        this.dataCredito = dataCredito;
        this.tipo = tipo;
        this.historico = historico;
        this.natureza = natureza;
        this.usaOrcamento = usaOrcamento;
        this.lei = lei;
        this.dataLei = dataLei;
        this.transposicao = transposicao;
        this.transposicaoFonte = transposicaoFonte;
        this.lancamento = -1;
    }

    public Date getDataLei() {
        return dataLei;
    }

    public void setDataLei(Date dataLei) {
        this.dataLei = dataLei;
    }

    public CreditoDotacaoPK getId() {
        return id;
    }

    public void setId(CreditoDotacaoPK id) {
        this.id = id;
    }

    public Date getDataCredito() {
        return dataCredito;
    }

    public void setDataCredito(Date dataCredito) {
        this.dataCredito = dataCredito;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getUsaOrcamento() {
        return usaOrcamento;
    }

    public void setUsaOrcamento(String usaOrcamento) {
        this.usaOrcamento = usaOrcamento;
    }

    public Integer getLei() {
        return lei;
    }

    public void setLei(Integer lei) {
        this.lei = lei;
    }

    public Integer getTransposicao() {
        return transposicao;
    }

    public void setTransposicao(Integer transposicao) {
        this.transposicao = transposicao;
    }

    public Integer getTransposicaoFonte() {
        return transposicaoFonte;
    }

    public void setTransposicaoFonte(Integer transposicaoFonte) {
        this.transposicaoFonte = transposicaoFonte;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }
}
