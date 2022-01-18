package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPEMPENHOS")
public class Empenhos implements Serializable {

    @EmbeddedId
    private EmpenhosPK id;

    private String tipo;
    private Integer ficha;

    @Temporal(TemporalType.DATE)
    private Date dataEmpenho;

    private Integer fornecedor;
    private Integer lancamento;
    private String desdobramento;
    private BigDecimal valorEmpenho;
    private Integer classeDespesa;

    public Empenhos() {
        this.id = new EmpenhosPK();
    }

    public Empenhos(Date ano, Integer empenho, String tipo, Integer ficha, Date dataEmpenho, Integer fornecedor, String desdobramento, Integer classeDespesa, BigDecimal valorEmpenho) {
        this.id = new EmpenhosPK(ano, empenho);
        this.tipo = tipo;
        this.ficha = ficha;
        this.dataEmpenho = dataEmpenho;
        this.lancamento = -1;
        this.fornecedor = fornecedor;
        this.desdobramento = desdobramento;
        this.valorEmpenho = valorEmpenho;
        this.classeDespesa = classeDespesa;
    }

    public EmpenhosPK getId() {
        return id;
    }

    public void setId(EmpenhosPK id) {
        this.id = id;
    }

    public Integer getFicha() {
        return ficha;
    }

    public void setFicha(Integer ficha) {
        this.ficha = ficha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataEmpenho() {
        return dataEmpenho;
    }

    public void setDataEmpenho(Date dataEmpenho) {
        this.dataEmpenho = dataEmpenho;
    }

    public Integer getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Integer fornecedor) {
        this.fornecedor = fornecedor;
    }

    public BigDecimal getValorEmpenho() {
        return valorEmpenho;
    }

    public void setValorEmpenho(BigDecimal valorEmpenho) {
        this.valorEmpenho = valorEmpenho;
    }

    public String getDesdobramento() {
        return desdobramento;
    }

    public void setDesdobramento(String desdobramento) {
        this.desdobramento = desdobramento;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public Integer getClasseDespesa() {
        return classeDespesa;
    }

    public void setClasseDespesa(Integer classeDespesa) {
        this.classeDespesa = classeDespesa;
    }
}
