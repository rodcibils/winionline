package negocio;

import java.util.Date;

public class Evidencia 
{
	public static final int MAX_EVIDENCIAS = 10;
	public static final String IMAGEN = "Imagen";
	public static final String VIDEO = "Video";
	
	private String path; // relative path a la evidencia de imagen
	private String tipo;
	private Date fecha;
	private String link; // link a la pagina que hostea evidencia de video
	
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
