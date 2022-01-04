package Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class LiquidaPagtoPK implements Serializable {

    @Temporal(TemporalType.DATE)
    private Date ano;
    private Integer empenho;
    private Integer liquidacao;
    private Integer pagamento;

    public LiquidaPagtoPK() {
    }

    public LiquidaPagtoPK(Date ano, Integer empenho, Integer liquidacao, Integer pagamento) {
        this.ano = ano;
        this.empenho = empenho;
        this.liquidacao = liquidacao;
        this.pagamento = pagamento;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public Integer getEmpenho() {
        return empenho;
    }

    public void setEmpenho(Integer empenho) {
        this.empenho = empenho;
    }

    public Integer getLiquidacao() {
        return liquidacao;
    }

    public void setLiquidacao(Integer liquidacao) {
        this.liquidacao = liquidacao;
    }

    public Integer getPagamento() {
        return pagamento;
    }

    public void setPagamento(Integer pagamento) {
        this.pagamento = pagamento;
    }

}
