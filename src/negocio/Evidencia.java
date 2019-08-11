package negocio;

import java.util.Date;

public class Evidencia 
{
	public static final String IMAGEN = "Imagen";
	public static final String VIDEO = "Video";
	
	private String path;
	private String tipo;
	private Date fecha;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
