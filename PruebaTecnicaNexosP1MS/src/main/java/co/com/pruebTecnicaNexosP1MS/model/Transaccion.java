/**
 * 
 */
package co.com.pruebTecnicaNexosP1MS.model;

import java.time.LocalDateTime;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
public class Transaccion {

	private Integer id;
	private Integer idTarjeta;
	private Double monto;
	private LocalDateTime fecha;
	private Integer estado;
	private Boolean esAprobada;

	public Transaccion() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(Integer idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Boolean getEsAprobada() {
		return esAprobada;
	}

	public void setEsAprobada(Boolean esAprobada) {
		this.esAprobada = esAprobada;
	}
}
