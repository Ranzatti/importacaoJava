package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRESTOSINSCRITOS")
public class RestosInscritos implements Serializable {

    @EmbeddedId
    private RestosInscritosPK id;

    private Integer ficha;
    private String origem;

    @Column(name = "CREDOR")
    private String nomeCredor;

    private Integer codCredor;

    @Column(name = "VALOR")
    private BigDecimal valorProcessado;

    @Column(name = "VALORNAOPROC")
    private BigDecimal valorNaoProcessado;

    private String subElemento;
    private BigDecimal valorEmpenho;

    @Temporal(TemporalType.DATE)
    private Date dataEmpenho;

    public RestosInscritos() {
        this.id = new RestosInscritosPK();
    }

    public RestosInscritos(Date ano, Integer empenho, String nomeCredor, Integer codCredor, BigDecimal valorProcessado, BigDecimal valorNaoProcessado, Integer ficha, String subElemento, Date dataEmpenho, BigDecimal valorEmpenho) {
        this.id = new RestosInscritosPK(ano, empenho);
        this.origem = "A";
        this.nomeCredor = nomeCredor;
        this.codCredor = codCredor;
        this.valorProcessado = valorProcessado;
        this.valorNaoProcessado = valorNaoProcessado;
        this.ficha = ficha;
        this.subElemento = subElemento;
        this.dataEmpenho = dataEmpenho;
        this.valorEmpenho = valorEmpenho;
    }

    public RestosInscritosPK getId() {
        return id;
    }

    public void setId(RestosInscritosPK id) {
        this.id = id;
    }

    public Integer getFicha() {
        return ficha;
    }

    public void setFicha(Integer ficha) {
        this.ficha = ficha;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getNomeCredor() {
        return nomeCredor;
    }

    public void setNomeCredor(String nomeCredor) {
        this.nomeCredor = nomeCredor;
    }

    public Integer getCodCredor() {
        return codCredor;
    }

    public void setCodCredor(Integer codCredor) {
        this.codCredor = codCredor;
    }

    public BigDecimal getValorProcessado() {
        return valorProcessado;
    }

    public void setValorProcessado(BigDecimal valorProcessado) {
        this.valorProcessado = valorProcessado;
    }

    public BigDecimal getValorNaoProcessado() {
        return valorNaoProcessado;
    }

    public void setValorNaoProcessado(BigDecimal valorNaoProcessado) {
        this.valorNaoProcessado = valorNaoProcessado;
    }

    public String getSubElemento() {
        return subElemento;
    }

    public void setSubElemento(String subElemento) {
        this.subElemento = subElemento;
    }

    public BigDecimal getValorEmpenho() {
        return valorEmpenho;
    }

    public void setValorEmpenho(BigDecimal valorEmpenho) {
        this.valorEmpenho = valorEmpenho;
    }

    public Date getDataEmpenho() {
        return dataEmpenho;
    }

    public void setDataEmpenho(Date dataEmpenho) {
        this.dataEmpenho = dataEmpenho;
    }
}
