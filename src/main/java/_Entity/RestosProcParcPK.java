package _Entity;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class RestosProcParcPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date anoEmpenho;
	private Integer empenho;
	private Integer parcela;

	public RestosProcParcPK() {
	}

	public RestosProcParcPK(Date anoEmpenho, Integer empenho, Integer parcela) {
		this.anoEmpenho = anoEmpenho;
		this.empenho = empenho;
		this.parcela = parcela;
	}

	public Date getAnoEmpenho() {
		return anoEmpenho;
	}

	public void setAnoEmpenho(Date anoEmpenho) {
		this.anoEmpenho = anoEmpenho;
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
}
