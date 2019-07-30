package negocio;

public class UsuarioEstadisticas {
	private int pos;
	private String nombre;
	private int puntos;
	private int partJugados;
	private int partGanados;
	private int partEmpatados;
	private int partPerdidos;
	private int golesFavor;
	private int golesContra;
	private int golesDiferencia;
	
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPartGanados() {
		return partGanados;
	}

	public void setPartGanados(int partGanados) {
		this.partGanados = partGanados;
	}

	public int getPartEmpatados() {
		return partEmpatados;
	}

	public void setPartEmpatados(int partEmpatados) {
		this.partEmpatados = partEmpatados;
	}

	public int getPartPerdidos() {
		return partPerdidos;
	}

	public void setPartPerdidos(int partPerdidos) {
		this.partPerdidos = partPerdidos;
	}

	public int getGolesFavor() {
		return golesFavor;
	}

	public void setGolesFavor(int golesFavor) {
		this.golesFavor = golesFavor;
	}

	public int getGolesContra() {
		return golesContra;
	}

	public void setGolesContra(int golesContra) {
		this.golesContra = golesContra;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getGolesDiferencia() {
		return golesDiferencia;
	}

	public void setGolesDiferencia(int golesDiferencia) {
		this.golesDiferencia = golesDiferencia;
	}

	public int getPartJugados() {
		return partJugados;
	}

	public void setPartJugados(int partJugados) {
		this.partJugados = partJugados;
	}
}
