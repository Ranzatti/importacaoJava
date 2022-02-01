package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPDEPOSITO")
public class Deposito implements Serializable {

	@EmbeddedId
	private DepositoPK id;
	private Integer banco;
	private String agencia;
	private String conta;
	private String historico;
	private String origem;
	private Date anoLancto;
	private Integer lancamento;
	private String tipoGuia;
	private Integer guia;
	private Integer transferencia;
	private BigDecimal valor;
	private Integer empenho;
	private Integer anulacao;
	private Integer parcela;
	private Integer versaoRecurso;
	private Integer fonteRecurso;
	private Integer caFixo;
	private Integer caVariavel;

	public Deposito() {this.id = new DepositoPK();
	}

	public Deposito(Integer fichaConta, Date data, Integer numero, Integer banco, String agencia, String conta, String historico, String origem, String tipoGuia, Integer guia,
					Integer transferencia, BigDecimal valor, Integer empenho, Integer anulacao, Integer parcela, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Date anoLancto, Integer lancamento) {
		this.id = new DepositoPK(fichaConta, data, numero);
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.historico = historico;
		this.origem = origem;
		this.anoLancto = anoLancto;
		this.lancamento = lancamento;
		this.tipoGuia = tipoGuia;
		this.guia = guia;
		this.transferencia = transferencia;
		this.valor = valor;
		this.empenho = empenho;
		this.anulacao = anulacao;
		this.parcela = parcela;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.caFixo = caFixo;
		this.caVariavel = caVariavel;
	}

	public DepositoPK getId() {
		return id;
	}

	public void setId(DepositoPK id) {
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

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
}
