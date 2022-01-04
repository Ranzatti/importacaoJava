package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPCREDITO")
public class Credito implements Serializable {

	@EmbeddedId
	private CreditoPK id;
	private Integer banco;
	private String agencia;
	private String conta;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date anoLancto;
	private Integer lancamento;
	private String tipoGuia;
	private Integer guia;
	private Integer versaoRecurso;
	private Integer fonteRecurso;
	private Integer caFixo;
	private Integer caVariavel;
	private String origem;
	private Integer empenho;
	private Integer anulacao;
	private Integer anulRecDedutora;
	private Integer parcela;
	private BigDecimal valor;

	public Credito() {
		this.id = new CreditoPK();
	}

	public Credito(Integer fichaConta, Date data, Integer numero,Integer banco, String agencia, String conta, String historico, Date anoLancto, Integer lancamento, String tipoGuia, Integer guia, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel,
				   String origem, Integer empenho, Integer anulacao, Integer anulRecDedutora, Integer parcela, BigDecimal valor) {
		this.id = new CreditoPK(fichaConta, data, numero);
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.historico = historico;
		this.anoLancto = anoLancto;
		this.lancamento = lancamento;
		this.tipoGuia = tipoGuia;
		this.guia = guia;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.caFixo = caFixo;
		this.caVariavel = caVariavel;
		this.origem = origem;
		this.empenho = empenho;
		this.anulacao = anulacao;
		this.anulRecDedutora = anulRecDedutora;
		this.parcela = parcela;
		this.valor = valor;
	}

	public CreditoPK getId() {
		return id;
	}

	public void setId(CreditoPK id) {
		this.id = id;
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
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

	public Integer getCaFixo() {
		return caFixo;
	}

	public void setCaFixo(Integer caFixo) {
		this.caFixo = caFixo;
	}

	public Integer getCaVariavel() {
		return caVariavel;
	}

	public void setCaVariavel(Integer caVariavel) {
		this.caVariavel = caVariavel;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
