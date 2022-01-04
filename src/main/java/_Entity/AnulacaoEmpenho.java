package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPANULACAOEMPENHO")
public class AnulacaoEmpenho implements Serializable {

	@EmbeddedId
	private AnulacaoEmpenhoPK id;
	
	@Temporal(TemporalType.DATE)
	private Date dataAnulacao;
	
	private BigDecimal valorAnulacao;
	private String historico;
	private String tipo;
	private Integer lancamento;

	public AnulacaoEmpenho() {
		this.id = new AnulacaoEmpenhoPK();
	}

	public AnulacaoEmpenho(Date ano, Integer empenho, Integer anulacao, Date dataAnulacao, BigDecimal valorAnulacao, String historico) {
		this.id = new AnulacaoEmpenhoPK(ano, empenho, anulacao);
		this.dataAnulacao = dataAnulacao;
		this.valorAnulacao = valorAnulacao;
		this.historico = historico;
		this.tipo = "X";
		this.lancamento = -1;
	}

	public AnulacaoEmpenhoPK getId() {
		return id;
	}

	public void setId(AnulacaoEmpenhoPK id) {
		this.id = id;
	}

	public Date getDataAnulacao() {
		return dataAnulacao;
	}

	public void setDataAnulacao(Date dataAnulacao) {
		this.dataAnulacao = dataAnulacao;
	}

	public BigDecimal getValorAnulacao() {
		return valorAnulacao;
	}

	public void setValorAnulacao(BigDecimal valorAnulacao) {
		this.valorAnulacao = valorAnulacao;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getLancamento() {
		return lancamento;
	}

	public void setLancamento(Integer lancamento) {
		this.lancamento = lancamento;
	}

}
