package Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CBPAUTPAGTO")
public class AutPagto {

    @EmbeddedId
    private AutPagtoPK id;

    @Temporal(TemporalType.DATE)
    private Date dataAutorizacao;

    private Integer lancamento;

    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    String descricao;

    public AutPagto() {
        this.id = new AutPagtoPK();
    }

    public AutPagto(Date ano, Integer numero, Date dataAutorizacao, Date dataPagamento, String descricao) {
        this.id = new AutPagtoPK(ano, numero);
        this.dataAutorizacao = dataAutorizacao;
        this.lancamento = -1;
        this.dataPagamento = dataPagamento;
        this.descricao = descricao;
    }

    public AutPagtoPK getId() {
        return id;
    }

    public Date getDataAutorizacao() {
        return dataAutorizacao;
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
