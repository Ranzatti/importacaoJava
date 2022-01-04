package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPDEBITO")
public class Debito implements Serializable {

	@EmbeddedId
	private DebitoPK id;
	private Integer banco;
	private String agencia;
	private String conta;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date anoLancto;
	private Integer lancamento;
	private String tipoAnulacaoRec;
	private Integer anulacaoReceita;
	private Integer versaoRecurso;
	private Integer fonteRecurso;
	private Integer caFixo;
	private Integer caVariavel;
	private String finalidade;
	private Integer autPagto;
	private Integer transferencia;
	private BigDecimal valor;

	public Debito() {
		this.id = new DebitoPK();
	}

	public Debito(Integer fichaConta, Date data, Integer numero, Integer banco, String agencia, String conta, String historico, Date anoLancto, Integer lancamento, String tipoAnulacaoRec,
				  Integer anulacaoReceita, BigDecimal valor, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, String finalidade, Integer autPagto, Integer transferencia) {
		this.id = new DebitoPK(fichaConta, data, numero);
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.historico = historico;
		this.anoLancto = anoLancto;
		this.lancamento = lancamento;
		this.tipoAnulacaoRec = tipoAnulacaoRec;
		this.anulacaoReceita = anulacaoReceita;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.caFixo = caFixo;
		this.caVariavel = caVariavel;
		this.finalidade = finalidade;
		this.autPagto = autPagto;
		this.transferencia = transferencia;
		this.valor = valor;
	}

	public DebitoPK getId() {
		return id;
	}

	public void setId(DebitoPK id) {
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

	public String getTipoAnulacaoRec() {
		return tipoAnulacaoRec;
	}

	public void setTipoAnulacaoRec(String tipoAnulacaoRec) {
		this.tipoAnulacaoRec = tipoAnulacaoRec;
	}

	public Integer getAnulacaoReceita() {
		return anulacaoReceita;
	}

	public void setAnulacaoReceita(Integer anulacaoReceita) {
		this.anulacaoReceita = anulacaoReceita;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public Integer getAutPagto() {
		return autPagto;
	}

	public void setAutPagto(Integer autPagto) {
		this.autPagto = autPagto;
	}

	public Integer getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Integer transferencia) {
		this.transferencia = transferencia;
	}

}
