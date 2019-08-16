package negocio;

public class Apelacion {
	public static int CANT_JUECES = 5;
	
	private Disputa disputa;
	private java.sql.Date fecha;
	private java.sql.Date cierre;

	public java.sql.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.sql.Date fecha) {
		this.fecha = fecha;
	}

	public java.sql.Date getCierre() {
		return cierre;
	}

	public void setCierre(java.sql.Date cierre) {
		this.cierre = cierre;
	}

	public Disputa getDisputa() {
		return disputa;
	}

	public void setDisputa(Disputa disputa) {
		this.disputa = disputa;
	}
}
