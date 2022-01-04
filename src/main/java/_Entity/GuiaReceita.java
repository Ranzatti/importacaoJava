package _Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPGUIARECEITA")
public class GuiaReceita implements Serializable {

	@EmbeddedId
	private GuiaReceitaPK id;
	private String historico;

	@Temporal(TemporalType.DATE)
	private Date dataGuia;

	@Temporal(TemporalType.DATE)
	private Date vencimento;

	@Temporal(TemporalType.DATE)
	private Date recebimento;

	private Integer contribuinte;
	private Integer lancamento;
	private String origem;
	private Integer autPagto;

	public GuiaReceita() {
		this.id = new GuiaReceitaPK();
	}

	public GuiaReceita(Date ano, String tipo,  Integer numero, Integer contribuinte, Date dataGuia, Date vencimento, Date recebimento, String historico, String origem, Integer autPagto) {
		this.id = new GuiaReceitaPK(ano, tipo, numero);
		this.historico = historico;
		this.dataGuia = dataGuia;
		this.vencimento = vencimento;
		this.recebimento = recebimento;
		this.contribuinte = contribuinte;
		this.lancamento = -1;
		this.origem = origem;
		this.autPagto = autPagto;
	}

	public GuiaReceitaPK getId() {
		return id;
	}

	public void setId(GuiaReceitaPK id) {
		this.id = id;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Date getDataGuia() {
		return dataGuia;
	}

	public void setDataGuia(Date dataGuia) {
		this.dataGuia = dataGuia;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public Date getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Date recebimento) {
		this.recebimento = recebimento;
	}

	public Integer getContribuinte() {
		return contribuinte;
	}

	public void setContribuinte(Integer contribuinte) {
		this.contribuinte = contribuinte;
	}

	public Integer getLancamento() {
		return lancamento;
	}

	public void setLancamento(Integer lancamento) {
		this.lancamento = lancamento;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public Integer getAutPagto() {
		return autPagto;
	}

	public void setAutPagto(Integer autPagto) {
		this.autPagto = autPagto;
	}
}
