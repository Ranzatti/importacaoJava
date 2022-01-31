package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name ="CBPANULACAOLIQUIDA")
public class AnulacaoLiquida implements Serializable {

	@EmbeddedId
	private AnulacaoLiquidaPK id;

	@Temporal(TemporalType.DATE)
	private Date data;
	private BigDecimal valor;
	private Integer lancamento;
	private String historico;

	public AnulacaoLiquida() { this.id = new AnulacaoLiquidaPK(); }

	public AnulacaoLiquida(Date ano, Integer empenho, Integer liquidacao, Integer anulacao, Date data, BigDecimal valor, String historico) {
		this.id = new AnulacaoLiquidaPK(ano, empenho, liquidacao, anulacao);
		this.data = data;
		this.valor = valor;
		this.lancamento = -1;
		this.historico = historico;
	}

	public AnulacaoLiquidaPK getId() {
		return id;
	}

	public void setId(AnulacaoLiquidaPK id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getLancamento() {
		return lancamento;
	}

	public void setLancamento(Integer lancamento) {
		this.lancamento = lancamento;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}
}
