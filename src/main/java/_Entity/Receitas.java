package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPRECEITAS")
public class Receitas implements Serializable {

	@EmbeddedId
	private ReceitasPK id;
	private Integer empresa;
	private String receita;
	private BigDecimal orcado;

	public Receitas() {
		this.id = new ReceitasPK();
	}

	public Receitas(Date ano, Integer empresa, Integer ficha, String receita, BigDecimal orcado) {
		this.id = new ReceitasPK(ano, ficha);
		this.empresa = empresa;
		this.receita = receita;
		this.orcado = orcado;
	}

	public ReceitasPK getId() {
		return id;
	}

	public void setId(ReceitasPK id) {
		this.id = id;
	}

	public Integer getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}

	public String getReceita() {
		return receita;
	}

	public void setReceita(String receita) {
		this.receita = receita;
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

}
