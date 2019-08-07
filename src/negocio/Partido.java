package negocio;

import java.sql.Date;

public class Partido {
	private int id;
	private Date fecha;
	private Estado estado;
	private Solicitud solicitud;
	private Resultado resultadoUno;
	private Resultado resultadoDos;
	private Usuario registro;
	
	// cant. maxima de dias que se puede editar el rtdo. de un partido
	public static final int MAX_DIAS_EDICION = 5;
	
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
	
	public void setResultadoUno(Resultado resultado){
		this.resultadoUno = resultado;
	}
	
	public Resultado getResultadoUno() {
		return resultadoUno;
	}
	
	public void setResultadoDos(Resultado resultado){
		this.resultadoDos = resultado;
	}
	
	public Resultado getResultadoDos() {
		return resultadoDos;
	}

	public Usuario getRegistro() {
		return registro;
	}

	public void setRegistro(Usuario registro) {
		this.registro = registro;
	}
}
