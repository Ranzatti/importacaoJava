package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class DescontosPagtoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private Integer empenho;
	private Integer parcela;
	private String tipoGuia;
	private Integer guiaReceita;

	public DescontosPagtoPK() {
	}

	public DescontosPagtoPK(Date ano, Integer empenho, Integer parcela, String tipoGuia, Integer guiaReceita) {
		this.ano = ano;
		this.empenho = empenho;
		this.parcela = parcela;
		this.tipoGuia = tipoGuia;
		this.guiaReceita = guiaReceita;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public Integer getEmpenho() {
		return empenho;
	}

	public void setEmpenho(Integer empenho) {
		this.empenho = empenho;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public Integer getGuiaReceita() {
		return guiaReceita;
	}

	public void setGuiaReceita(Integer guiaReceita) {
		this.guiaReceita = guiaReceita;
	}
}
