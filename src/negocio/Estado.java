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
	public static final int PARTIDO_PENDIENTE = 8;
	public static final int PARTIDO_FINALIZADO = 9;
	public static final int PARTIDO_RECHAZADO = 10;
	public static final int PARTIDO_DISPUTADO = 11;
	public static final int DISPUTA_EN_CURSO = 12;
	public static final int DISPUTA_CERRADA = 13;
	public static final int DISPUTA_APELADA = 14;
	public static final int APELACION_EN_CURSO = 15;
	public static final int APELACION_CERRADA = 16;
	
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
