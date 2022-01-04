package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPITENSEMPENHO")
public class ItensEmpenho implements Serializable {

    @EmbeddedId
    private ItensEmpenhoPK id;
    private Integer item;
    private String tipo;
    private String descricao;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public ItensEmpenho() {
        this.id = new ItensEmpenhoPK();
    }

    public ItensEmpenho(Date ano, Integer empenho, String descricao, BigDecimal valorEmpenho) {
        this.id = new ItensEmpenhoPK(ano, empenho);
        this.item = 1;
        this.tipo = "P";
        this.descricao = descricao;
        this.valorUnitario = valorEmpenho;
        this.valorTotal = valorEmpenho;
    }

    public ItensEmpenhoPK getId() {
        return id;
    }

    public void setId(ItensEmpenhoPK id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

}
