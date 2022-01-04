package Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPTABDESPESAS")
public class TabDespesa implements Serializable {

	@EmbeddedId
	private TabDespesaPK id;
	private String tipoConta;
	private String nome;
	private String natureza;
	private String categoria;
	private String grupo;
	private String modalidade;
	private String elemento;
	private String desdobramento;

	public TabDespesa() {
		this.id = new TabDespesaPK();
	}

	public TabDespesa(Date ano, String despesa, String tipoConta, String nome, String natureza, String categoria, String grupo, String modalidade, String elemento, String desdobramento) {
		this.id = new TabDespesaPK(ano, despesa);
		this.tipoConta = tipoConta;
		this.nome = nome;
		this.natureza = natureza;
		this.categoria = categoria;
		this.grupo = grupo;
		this.modalidade = modalidade;
		this.elemento = elemento;
		this.desdobramento = desdobramento;
	}

	public TabDespesaPK getId() {
		return id;
	}

	public void setId(TabDespesaPK id) {
		this.id = id;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public String getDesdobramento() {
		return desdobramento;
	}

	public void setDesdobramento(String desdobramento) {
		this.desdobramento = desdobramento;
	}
}
