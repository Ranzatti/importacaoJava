package _Entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class RecContaBancoPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private String tipo;
	private Integer guia;

	@Column(name = "FICHA")
	private Integer fichaReceita;
	private Integer versaoRecurso;

	@Column(name = "RECURSO")
	private Integer fonteRecurso;
	private Integer caFixo;
	private Integer caVariavel;
	private Integer fichaBanco;
	private Integer versaoRecursoBanco;
	private Integer fonteRecursoBanco;
	private Integer caFixoBanco;
	private Integer caVariavelBanco;
	private Integer item;

	public RecContaBancoPK() {
	}

	public RecContaBancoPK(Date ano, String tipo, Integer guia, Integer fichaReceita, Integer versaoRecurso, Integer fonteRecurso, Integer caFixo, Integer caVariavel, Integer item, Integer fichaBanco,
						   Integer versaoRecursoBanco, Integer fonteRecursoBanco, Integer caFixoBanco, Integer caVariavelBanco) {
		this.ano = ano;
		this.tipo = tipo;
		this.guia = guia;
		this.fichaBanco = fichaBanco;
		this.fichaReceita = fichaReceita;
		this.versaoRecurso = versaoRecurso;
		this.fonteRecurso = fonteRecurso;
		this.caFixo = caFixo;
		this.caVariavel = caVariavel;
		this.item = item;
		this.versaoRecursoBanco = versaoRecursoBanco;
		this.fonteRecursoBanco = fonteRecursoBanco;
		this.caFixoBanco = caFixoBanco;
		this.caVariavelBanco = caVariavelBanco;
	}

	public Date getAno() {
		return ano;
	}

	public void setAno(Date ano) {
		this.ano = ano;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getGuia() {
		return guia;
	}

	public void setGuia(Integer guia) {
		this.guia = guia;
	}

	public Integer getFichaBanco() {
		return fichaBanco;
	}

	public void setFichaBanco(Integer fichaBanco) {
		this.fichaBanco = fichaBanco;
	}

	public Integer getFichaReceita() {
		return fichaReceita;
	}

	public void setFichaReceita(Integer fichaReceita) {
		this.fichaReceita = fichaReceita;
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

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Integer getVersaoRecursoBanco() {
		return versaoRecursoBanco;
	}

	public void setVersaoRecursoBanco(Integer versaoRecursoBanco) {
		this.versaoRecursoBanco = versaoRecursoBanco;
	}

	public Integer getFonteRecursoBanco() {
		return fonteRecursoBanco;
	}

	public void setFonteRecursoBanco(Integer fonteRecursoBanco) {
		this.fonteRecursoBanco = fonteRecursoBanco;
	}

	public Integer getCaFixoBanco() {
		return caFixoBanco;
	}

	public void setCaFixoBanco(Integer caFixoBanco) {
		this.caFixoBanco = caFixoBanco;
	}

	public Integer getCaVariavelBanco() {
		return caVariavelBanco;
	}

	public void setCaVariavelBanco(Integer caVariavelBanco) {
		this.caVariavelBanco = caVariavelBanco;
	}
}
