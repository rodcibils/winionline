package negocio;

public class Disputa {
	public static final int MAX_DIAS_DISPUTA = 10;
	
	private Partido partido;
	private java.sql.Date fecha;
	private java.sql.Date vencimiento;
	private Estado estado;
	
	public Partido getPartido() {
		return partido;
	}
	
	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public java.sql.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.sql.Date fecha) {
		this.fecha = fecha;
	}

	public java.sql.Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(java.sql.Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
