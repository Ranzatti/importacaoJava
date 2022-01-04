package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPSAIDASCAIXAS")
public class SaidasCaixa implements Serializable {

	@EmbeddedId
	private SaidasCaixaPK id;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date anoLancto;
	private Integer lancamento;
	private String tipoAnulacaoRec;
	private Integer anulacaoReceita;
	private Integer transferencia;
	private Integer autPagto;
	private Integer versaoRecurso;
	private Integer fonteRecurso;
	private BigDecimal valor;

	public SaidasCaixa() {
		this.id = new SaidasCaixaPK();
	}

	public SaidasCaixa(Date data, Integer saida, String historico, Date anoLancto, Integer lancamento, String tipoAnulacaoRec, Integer anulacaoReceita, BigDecimal valor, Integer transferencia, Integer autPagto, Integer versaoRecurso, Integer fonteRecurso) {
		this.id = new SaidasCaixaPK(data, saida);
		this.historico = historico;
		this.anoLancto = anoLancto;
		this.lancamento = lancamento;
		this.tipoAnulacaoRec = tipoAnulacaoRec;
		this.anulacaoReceita = anulacaoReceita;
		this.valor = valor;
		this.transferencia = transferencia;
		this.autPagto = autPagto;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
	}

	public SaidasCaixaPK getId() {
		return id;
	}

	public void setId(SaidasCaixaPK id) {
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

	public Integer getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Integer transferencia) {
		this.transferencia = transferencia;
	}

	public Integer getAutPagto() {
		return autPagto;
	}

	public void setAutPagto(Integer autPagto) {
		this.autPagto = autPagto;
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
}
