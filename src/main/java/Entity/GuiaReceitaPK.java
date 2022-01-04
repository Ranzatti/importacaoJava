package Entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class GuiaReceitaPK implements Serializable {

	@Temporal(TemporalType.DATE)
	private Date ano;
	private String tipo;
	private Integer numero;

	public GuiaReceitaPK() {
	}

	public GuiaReceitaPK(Date ano, String tipo, Integer numero) {
		this.ano = ano;
		this.tipo = tipo;
		this.numero = numero;
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

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}
