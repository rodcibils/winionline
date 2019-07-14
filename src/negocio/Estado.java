package negocio;

public class Estado {
	private int id;
	private String descripcion;
	
	public static final int USUARIO_ACTIVO = 1;
	public static final int USUARIO_ELIMINADO = 2;
	public static final int LIGA_NO_INICIADA = 3;
	public static final int LIGA_INICIADA = 4;
	public static final int LIGA_FINALIZADA = 5;
	public static final int SOLICITUD_PENDIENTE = 6;
	public static final int SOLICITUD_ACEPTADA = 7;
	public static final int SOLICITUD_RECHAZADA = 8;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
