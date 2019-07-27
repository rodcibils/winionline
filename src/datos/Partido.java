package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Partido {
	private static Partido instance = null;
	
	public static Partido getInstance() {
		if(instance == null) {
			instance = new Partido();
		}
		
		return instance;
	}
	
	public void createAmistoso(int idSolicitud) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO partidos(estado, solicitud) "
				+ "VALUES (?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.PARTIDO_PENDIENTE);
		stmt.setInt(2, idSolicitud);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void reject(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE partidos SET estado=?, fecha=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.PARTIDO_RECHAZADO);
		java.util.Date today = new java.util.Date();
		java.sql.Date sqlToday = new java.sql.Date(today.getTime());
		stmt.setDate(2, sqlToday);
		stmt.setInt(3, id);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getCountAmistososPendientes(int jugador) 
			throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes = 
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		int count = 0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_PENDIENTE);
			stmt.setInt(2, solicitud.getId());
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				++count;
			}
			
			rs.close();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountAmistososPendientes(int jugador, String toSearch) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes =
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		int count=0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_PENDIENTE);
			stmt.setInt(2, solicitud.getId());
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if((solicitud.getJugadorDos().getId() != jugador 
						&& (solicitud.getJugadorDos().getNombre().contains(toSearch)) ||
						solicitud.getJugadorDos().getApodo().contains(toSearch)) ||
						(solicitud.getJugadorUno().getId() != jugador 
						&& (solicitud.getJugadorUno().getNombre().contains(toSearch)) ||
						solicitud.getJugadorUno().getApodo().contains(toSearch)))
				{
					++count;
				}
			}
			
			rs.close();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public ArrayList<negocio.Partido> getAmistososPendientes(int jugador, int skip, int limit) 
			throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes = 
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		ArrayList<negocio.Partido> amistososPendientes = new ArrayList<negocio.Partido>();
		int count = 0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_PENDIENTE);
			stmt.setInt(2, solicitud.getId());
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(count >= skip) {
					if(amistososPendientes.size() <= limit) {
						negocio.Partido amistosoPendiente = new negocio.Partido();
						amistosoPendiente.setId(rs.getInt(1));
						amistosoPendiente.setSolicitud(solicitud);
						amistososPendientes.add(amistosoPendiente);
					} else {
						rs.close();
						stmt.close();
						break;
					}
				}
				
				++count;
			}
			
			rs.close();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
		
		return amistososPendientes;
	}
	
	public ArrayList<negocio.Partido> getAmistososPendientes(int jugador, int skip, 
			int limit, String toSearch) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes = 
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		ArrayList<negocio.Partido> amistososPendientes = new ArrayList<negocio.Partido>();
		int count = 0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_PENDIENTE);
			stmt.setInt(2, solicitud.getId());
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if((solicitud.getJugadorDos().getId() != jugador 
						&& (solicitud.getJugadorDos().getNombre().contains(toSearch)) ||
						solicitud.getJugadorDos().getApodo().contains(toSearch)) ||
						(solicitud.getJugadorUno().getId() != jugador 
						&& (solicitud.getJugadorUno().getNombre().contains(toSearch)) ||
						solicitud.getJugadorUno().getApodo().contains(toSearch)))
				{
					if(count >= skip) {
						if(amistososPendientes.size() <= limit) {
							negocio.Partido amistosoPendiente = new negocio.Partido();
							amistosoPendiente.setId(rs.getInt(1));
							amistosoPendiente.setSolicitud(solicitud);
							amistososPendientes.add(amistosoPendiente);
						} else {
							rs.close();
							stmt.close();
							break;
						}
					}
					
					++count;
				}
			}
			
			rs.close();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
		
		return amistososPendientes;
	}
}
