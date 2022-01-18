package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPEMPCOMPLEMENTAR")
public class EmpenhoComplemento implements Serializable {

    @EmbeddedId
    private EmpenhoComplementoPK id;
    private Date data;
    private Integer lancamento;
    private String descricao;
    private BigDecimal valor;

    public EmpenhoComplemento() {
        this.id = new EmpenhoComplementoPK();
    }

    public EmpenhoComplemento(Date ano, Integer empenho, Integer complemento, Date data, String descricao, BigDecimal valor) {
        this.id = new EmpenhoComplementoPK(ano, empenho, complemento);
        this.data = data;
        this.lancamento = -1;
        this.descricao = descricao;
        this.valor = valor;
    }

    public EmpenhoComplementoPK getId() {
        return id;
    }

    public void setId(EmpenhoComplementoPK id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
