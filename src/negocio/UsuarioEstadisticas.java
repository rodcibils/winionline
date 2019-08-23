package negocio;

import java.util.ArrayList;

public class UsuarioEstadisticas {
	private int idUsuario;
	private String nombre;
	private int puntos;
	private int partJugados;
	private int partGanados;
	private int partEmpatados;
	private int partPerdidos;
	private int golesFavor;
	private int golesContra;
	private int golesDiferencia;
	private boolean puedeJugar;
	private int posicion;
	
	public static ArrayList<UsuarioEstadisticas> 
		determinarPosiciones(ArrayList<UsuarioEstadisticas> jugadores)
	{
		for(int i=0; i<(jugadores.size() - 1); ++i) {
			UsuarioEstadisticas jugadorUno = jugadores.get(i);
			for(int j=1; j<jugadores.size(); ++j) {
				UsuarioEstadisticas jugadorDos = jugadores.get(j);
				
				if(jugadorDos.getPuntos() > jugadorUno.getPuntos()) {
					jugadores.set(i, jugadorDos);
					jugadores.set(j, jugadorUno);
				} else if(jugadorDos.getPuntos() == jugadorUno.getPuntos()) {
					if(jugadorDos.getGolesDiferencia() > jugadorUno.getGolesDiferencia()) {
						jugadores.set(i, jugadorDos);
						jugadores.set(j, jugadorUno);
					} else if(jugadorDos.getGolesDiferencia() == jugadorUno.getGolesDiferencia()) {
						if(jugadorDos.getGolesFavor() > jugadorUno.getGolesFavor()) {
							jugadores.set(i, jugadorDos);
							jugadores.set(j, jugadorUno);
						} else if(jugadorDos.getGolesFavor() == jugadorUno.getGolesFavor()) {
							if(jugadorDos.getGolesContra() < jugadorUno.getGolesContra()) {
								jugadores.set(i, jugadorDos);
								jugadores.set(j, jugadorUno);
							}
						}
					}
				}
			}
		}
		
		return jugadores;
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

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int id) {
		this.idUsuario = id;
	}

	public boolean isPuedeJugar() {
		return puedeJugar;
	}

	public void setPuedeJugar(boolean puedeJugar) {
		this.puedeJugar = puedeJugar;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}
