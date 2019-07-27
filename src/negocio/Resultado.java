package negocio;

public class Resultado {
	private Usuario jugador;
	private Partido partido;
	private int goles;
	
	public Usuario getJugador() {
		return jugador;
	}
	
	public void setJugador(Usuario jugador) {
		this.jugador = jugador;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}
}
