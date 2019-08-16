package negocio;

public class Disputa {
	public static final int MAX_DIAS_DISPUTA = 10;
	public static final int ANTIGUEDAD_PARA_VOTAR = 15;
	public static final int MAX_DIAS_APELACION = 10;
	
	private Partido partido;
	private java.sql.Date fecha;
	private java.sql.Date vencimiento;
	private Estado estado;
	
	private int votosUno;
	private int votosDos;
	private boolean apelable;
	
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

	public int getVotosUno() {
		return votosUno;
	}

	public void setVotosUno(int votosUno) {
		this.votosUno = votosUno;
	}

	public int getVotosDos() {
		return votosDos;
	}

	public void setVotosDos(int votosDos) {
		this.votosDos = votosDos;
	}

	public boolean isApelable() {
		return apelable;
	}

	public void setApelable(boolean apelable) {
		this.apelable = apelable;
	}
}
