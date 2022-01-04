package Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CBPCONTAS")
public class ContasBancarias implements Serializable {

	@Id
	private Integer ficha;
	private Integer banco;
	private String agencia;
	private String codigo;
	private String dv;
	private String nome;
	private String tipoConta;
	private String titular;
	private Integer bancoAudesp;
	private String agenciaAudesp;
	private String contaAudesp;
	private Integer empresa;

	@Temporal(TemporalType.DATE)
	private Date encerramento;

	@Temporal(TemporalType.DATE)
	private Date abertura;

	public ContasBancarias() {
	}

	public ContasBancarias(Integer ficha, Integer banco, String agencia, String codigo, String dv, String nome, String tipoConta, String titular, Integer bancoAudesp, String agenciaAudesp, String contaAudesp, Integer empresa, Date encerramento, Date abertura) {
		this.ficha = ficha;
		this.banco = banco;
		this.agencia = agencia;
		this.codigo = codigo;
		this.dv = dv;
		this.nome = nome;
		this.tipoConta = tipoConta;
		this.titular = titular;
		this.bancoAudesp = bancoAudesp;
		this.agenciaAudesp = agenciaAudesp;
		this.contaAudesp = contaAudesp;
		this.empresa = empresa;
		this.encerramento = encerramento;
		this.abertura = abertura;
	}

	public Integer getFicha() {
		return ficha;
	}

	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Integer getBancoAudesp() {
		return bancoAudesp;
	}

	public void setBancoAudesp(Integer bancoAudesp) {
		this.bancoAudesp = bancoAudesp;
	}

	public String getAgenciaAudesp() {
		return agenciaAudesp;
	}

	public void setAgenciaAudesp(String agenciaAudesp) {
		this.agenciaAudesp = agenciaAudesp;
	}

	public String getContaAudesp() {
		return contaAudesp;
	}

	public void setContaAudesp(String contaAudesp) {
		this.contaAudesp = contaAudesp;
	}

	public Date getEncerramento() {
		return encerramento;
	}

	public void setEncerramento(Date encerramento) {
		this.encerramento = encerramento;
	}

	public Integer getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}

	public Date getAbertura() {
		return abertura;
	}

	public void setAbertura(Date abertura) {
		this.abertura = abertura;
	}
}
