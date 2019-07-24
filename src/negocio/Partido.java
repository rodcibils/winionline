package negocio;

import java.sql.Date;

public class Partido {
	private int id;
	private Date fecha;
	private Estado estado;
	private Solicitud solicitud;
	
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
		Date sqlFecha = new Date(fecha.getTime());
		this.fecha = sqlFecha;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}
}
