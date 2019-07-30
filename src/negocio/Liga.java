package negocio;

import java.sql.Date;

public class Liga {
	private int id;
	private String nombre;
	private int temporada;
	private Date inicio;
	private Date fin;
	private Estado estado;
	private int CantidadInscriptos;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTemporada() {
		return temporada;
	}

	public void setTemporada(int temporada) {
		this.temporada = temporada;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	public void setInicio(java.util.Date inicio) {
		Date sqlDate = new Date(inicio.getTime());
		this.inicio = sqlDate;
	}

	public Date getFin() {
		return fin;
	}
	
	public void setFin(java.util.Date fin) {
		Date sqlDate = new Date(fin.getTime());
		this.fin = sqlDate;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public int getCantidadInscriptos() {
		return CantidadInscriptos;
	}
	
	public void setCantidadInscriptos(int cantidadInscriptos) {
		this.CantidadInscriptos = cantidadInscriptos;
	}
	
}
