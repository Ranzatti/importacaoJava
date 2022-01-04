package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPLIQUIDARESTOS")
public class LiquidaRestos implements Serializable {

	@EmbeddedId
	private LiquidaRestosPK id;

	@Temporal(TemporalType.DATE)
	private Date dataLiquidacao;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date vencimento;
	private String liquidante;

	@Temporal(TemporalType.DATE)
	private Date anoFato;
	private Integer fato;
	private BigDecimal valor;

	public LiquidaRestos() {
		this.id = new LiquidaRestosPK();
	}

	public LiquidaRestos(Date anoEmpenho, Integer empenho, Integer liquidacao, Date dataLiquidacao, String historico, Date vencimento, String liquidante, Date anoFato, Integer fato, BigDecimal valor) {
		this.id = new LiquidaRestosPK(anoEmpenho, empenho, liquidacao);
		this.dataLiquidacao = dataLiquidacao;
		this.historico = historico;
		this.vencimento = vencimento;
		this.liquidante = liquidante;
		this.anoFato = anoFato;
		this.fato = fato;
		this.valor = valor;
	}

	public LiquidaRestosPK getId() {
		return id;
	}

	public void setId(LiquidaRestosPK id) {
		this.id = id;
	}

	public Date getDataLiquidacao() {
		return dataLiquidacao;
	}

	public void setDataLiquidacao(Date dataLiquidacao) {
		this.dataLiquidacao = dataLiquidacao;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public String getLiquidante() {
		return liquidante;
	}

	public void setLiquidante(String liquidante) {
		this.liquidante = liquidante;
	}

	public Date getAnoFato() {
		return anoFato;
	}

	public void setAnoFato(Date anoFato) {
		this.anoFato = anoFato;
	}

	public Integer getFato() {
		return fato;
	}

	public void setFato(Integer fato) {
		this.fato = fato;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
