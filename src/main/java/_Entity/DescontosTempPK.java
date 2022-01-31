package _Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class DescontosTempPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private String tipoDoc;
	private Integer documento;
	private Integer parcela;
	private String tipoDesc;
	private Integer ficha;
	private Integer versaoRecurso;

	@Column(name = "RECURSO")
	private Integer fonteRecurso;
	private Integer caFixo;
	private Integer caVariavel;

	public DescontosTempPK() {
	}

	public DescontosTempPK(Date ano, String tipoDoc, Integer documento, Integer parcela, String tipoDesc, Integer ficha, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel) {
		this.ano = ano;
		this.tipoDoc = tipoDoc;
		this.documento = documento;
		this.parcela = parcela;
		this.tipoDesc = tipoDesc;
		this.ficha = ficha;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.caFixo = caFixo;
		this.caVariavel = caVariavel;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public Integer getDocumento() {
		return documento;
	}

	public void setDocumento(Integer documento) {
		this.documento = documento;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public String getTipoDesc() {
		return tipoDesc;
	}

	public void setTipoDesc(String tipoDesc) {
		this.tipoDesc = tipoDesc;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
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
