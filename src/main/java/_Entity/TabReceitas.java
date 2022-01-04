package _Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CBPTABRECEITAS")
public class TabReceitas implements Serializable {

	@EmbeddedId
	private TabReceitasPK id;

	private String tipoConta;
	private String nome;
	private String natureza;
	private String OrigemNaLei;
	private BigDecimal percentual;
	private Integer RecDividaAtiva;
	private Integer deducao;

	public TabReceitas() {
		this.id = new TabReceitasPK();
	}

	public TabReceitas(Date ano, String receita, String tipoConta, String nome, String natureza, String origemNaLei, BigDecimal percentual, Integer recDividaAtiva, Integer deducao) {
		this.id = new TabReceitasPK(ano, receita);
		this.tipoConta = tipoConta;
		this.nome = nome;
		this.natureza = natureza;
		OrigemNaLei = origemNaLei;
		this.percentual = percentual;
		RecDividaAtiva = recDividaAtiva;
		this.deducao = deducao;
	}

	public TabReceitasPK getId() {
		return id;
	}

	public void setId(TabReceitasPK id) {
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

	public String getOrigemNaLei() {
		return OrigemNaLei;
	}

	public void setOrigemNaLei(String origemNaLei) {
		OrigemNaLei = origemNaLei;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public Integer getRecDividaAtiva() {
		return RecDividaAtiva;
	}

	public void setRecDividaAtiva(Integer recDividaAtiva) {
		RecDividaAtiva = recDividaAtiva;
	}

	public Integer getDeducao() {
		return deducao;
	}

	public void setDeducao(Integer deducao) {
		this.deducao = deducao;
	}

}
