package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPLIQUIDACOES")
public class LiquidacaoEmpenho implements Serializable {

	@EmbeddedId
	private LiquidacaoEmpenhoPK id;

	@Temporal(TemporalType.DATE)
	private Date dataLiquidacao;

	private BigDecimal valorLiquidacao;
	private String historico;
	private String liquidante;

	@Temporal(TemporalType.DATE)
	private Date anoLancto;
	private Integer lancamento;

	public LiquidacaoEmpenho() {
		this.id = new LiquidacaoEmpenhoPK();
	}

	public LiquidacaoEmpenho(Date ano, Integer empenho, Integer liquidacao, Date dataLiquidacao, BigDecimal valorLiquidacao, String historico) {
		this.id = new LiquidacaoEmpenhoPK(ano, empenho, liquidacao);
		this.dataLiquidacao = dataLiquidacao;
		this.valorLiquidacao = valorLiquidacao;
		this.historico = historico;
		this.liquidante = null;
		this.anoLancto = ano;
		this.lancamento = -1;
	}

	public LiquidacaoEmpenhoPK getId() {
		return id;
	}

	public void setId(LiquidacaoEmpenhoPK id) {
		this.id = id;
	}

	public Date getDataLiquidacao() {
		return dataLiquidacao;
	}

	public void setDataLiquidacao(Date dataLiquidacao) {
		this.dataLiquidacao = dataLiquidacao;
	}

	public BigDecimal getValorLiquidacao() {
		return valorLiquidacao;
	}

	public void setValorLiquidacao(BigDecimal valorLiquidacao) {
		this.valorLiquidacao = valorLiquidacao;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public String getLiquidante() {
		return liquidante;
	}

	public void setLiquidante(String liquidante) {
		this.liquidante = liquidante;
	}

	public Date getAnoLancto() {
		return anoLancto;
	}

	public void setAnoLancto(Date anoLancto) {
		this.anoLancto = anoLancto;
	}

	public Integer getLancamento() {
		return lancamento;
	}

	public void setLancamento(Integer lancamento) {
		this.lancamento = lancamento;
	}
}
