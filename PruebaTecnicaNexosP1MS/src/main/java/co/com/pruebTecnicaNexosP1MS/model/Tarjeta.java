/**
 * 
 */
package co.com.pruebTecnicaNexosP1MS.model;

import java.time.LocalDateTime;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
public class Tarjeta {

	private Integer id;
	private Integer tipo;
	private String numero;
	private LocalDateTime vencimiento;
	private Double cupo;
	private Boolean esActivo;
	private Boolean esBloqueado;
	private String nombreCliente;
	private String apellidoCliente;

	public Tarjeta() {
	}

	public Tarjeta(Integer id, Integer tipo, String numero, LocalDateTime vencimiento, Double cupo, Boolean esActivo,
			Boolean esBloqueado, String nombreCliente, String apellidoCliente) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.numero = numero;
		this.vencimiento = vencimiento;
		this.cupo = cupo;
		this.esActivo = esActivo;
		this.esBloqueado = esBloqueado;
		this.nombreCliente = nombreCliente;
		this.apellidoCliente = apellidoCliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public LocalDateTime getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(LocalDateTime vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Double getCupo() {
		return cupo;
	}

	public void setCupo(Double cupo) {
		this.cupo = cupo;
	}

	public Boolean getEsActivo() {
		return esActivo;
	}

	public void setEsActivo(Boolean esActivo) {
		this.esActivo = esActivo;
	}

	public Boolean getEsBloqueado() {
		return esBloqueado;
	}

	public void setEsBloqueado(Boolean esBloqueado) {
		this.esBloqueado = esBloqueado;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getApellidoCliente() {
		return apellidoCliente;
	}

	public void setApellidoCliente(String apellidoCliente) {
		this.apellidoCliente = apellidoCliente;
	}

}
