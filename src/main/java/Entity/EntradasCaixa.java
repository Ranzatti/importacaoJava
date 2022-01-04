package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPENTRADASCAIXAS")
public class EntradasCaixa implements Serializable {

	@EmbeddedId
	private EntradasCaixaPK id;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date anoLancto;
	private Integer lancamento;
	private String tipoGuia;
	private Integer guia;
	private Integer transferencia;
	private Integer empenho;
	private Integer anulacao;
	private Integer anulRecDedutora;
	private Integer parcela;
	private Integer versaoRecurso;
	private Integer fonteRecurso;
	private Integer regulaFUNDEB;
	private BigDecimal valor;

	public EntradasCaixa() {
		this.id = new EntradasCaixaPK();
	}

	public EntradasCaixa(Date data, Integer entrada, String historico, Date anoLancto, Integer lancamento, String tipoGuia, Integer guia, Integer transferencia, Integer empenho, Integer anulacao, Integer anulRecDedutora, Integer parcela,
						 Integer versaoRecurso, Integer fonteRecurso, Integer regulaFUNDEB, BigDecimal valor) {
		this.id = new EntradasCaixaPK(data, entrada);
		this.historico = historico;
		this.anoLancto = anoLancto;
		this.lancamento = lancamento;
		this.tipoGuia = tipoGuia;
		this.guia = guia;
		this.transferencia = transferencia;
		this.empenho = empenho;
		this.anulacao = anulacao;
		this.anulRecDedutora = anulRecDedutora;
		this.parcela = parcela;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.regulaFUNDEB = regulaFUNDEB;
		this.valor = valor;
	}

	public EntradasCaixaPK getId() {
		return id;
	}

	public void setId(EntradasCaixaPK id) {
		this.id = id;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
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

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public Integer getGuia() {
		return guia;
	}

	public void setGuia(Integer guia) {
		this.guia = guia;
	}

	public Integer getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Integer transferencia) {
		this.transferencia = transferencia;
	}

	public Integer getEmpenho() {
		return empenho;
	}

	public void setEmpenho(Integer empenho) {
		this.empenho = empenho;
	}

	public Integer getAnulacao() {
		return anulacao;
	}

	public void setAnulacao(Integer anulacao) {
		this.anulacao = anulacao;
	}

	public Integer getAnulRecDedutora() {
		return anulRecDedutora;
	}

	public void setAnulRecDedutora(Integer anulRecDedutora) {
		this.anulRecDedutora = anulRecDedutora;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public Integer getVersaoRecurso() {
		return versaoRecurso;
	}

	public void setVersaoRecurso(Integer versaoRecurso) {
		this.versaoRecurso = versaoRecurso;
	}

	public Integer getFonteRecurso() {
		return fonteRecurso;
	}

	public void setFonteRecurso(Integer fonteRecurso) {
		this.fonteRecurso = fonteRecurso;
	}

	public Integer getRegulaFUNDEB() {
		return regulaFUNDEB;
	}

	public void setRegulaFUNDEB(Integer regulaFUNDEB) {
		this.regulaFUNDEB = regulaFUNDEB;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
