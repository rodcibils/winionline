package negocio;

import java.sql.Date;

public class Solicitud {
	private int id;
	private Date fecha;
	private negocio.Estado estado;
	private negocio.Usuario jugador_uno;
	private negocio.Usuario jugador_dos;
	private negocio.Liga liga;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setFecha(java.util.Date fecha) {
		Date sqlDate = new Date(fecha.getTime());
		this.fecha = sqlDate;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Usuario getJugadorUno() {
		return jugador_uno;
	}

	public void setJugadorUno(Usuario jugador_uno) {
		this.jugador_uno = jugador_uno;
	}

	public Usuario getJugadorDos() {
		return jugador_dos;
	}

	public void setJugadorDos(Usuario jugador_dos) {
		this.jugador_dos = jugador_dos;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}
	
	
}
