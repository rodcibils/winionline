package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Partido {
	private static Partido instance = null;
	
	public static Partido getInstance() {
		if(instance == null) {
			instance = new Partido();
		}
		
		return instance;
	}
	
	public void finalizarPartido(int id, int registro) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE partidos SET fecha=?, estado=?, registro=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		Date fecha = new Date();
		java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());
		stmt.setDate(1, sqlFecha);
		stmt.setInt(2, negocio.Estado.PARTIDO_FINALIZADO);
		stmt.setInt(3, registro);
		stmt.setInt(4, id);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
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
	
	public int getCountAmistosos(int jugador) 
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
			stmt.setInt(1, negocio.Estado.PARTIDO_FINALIZADO);
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
	
	public int getCountAmistosos(int jugador, String toSearch) 
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
			stmt.setInt(1, negocio.Estado.PARTIDO_FINALIZADO);
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
	
	public ArrayList<negocio.Partido> getAmistosos(int jugador, int skip,
			int limit) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes =
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<>();
		int count = 0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_FINALIZADO);
			stmt.setInt(2, solicitud.getId());
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(count >= skip) {
					if(amistosos.size() <= limit) {
						negocio.Partido amistoso = new negocio.Partido();
						amistoso.setId(rs.getInt(1));
						amistoso.setSolicitud(solicitud);
						amistoso.setFecha(rs.getDate(2));
						amistoso.setResultadoUno(datos.Resultado.getInstance()
								.getOne(solicitud.getJugadorUno(), amistoso));
						amistoso.setResultadoDos(datos.Resultado.getInstance()
								.getOne(solicitud.getJugadorDos(), amistoso));
						amistoso.setRegistro(datos.Usuario.getInstance().getOne(rs.getInt(5)));
						amistosos.add(amistoso);
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
		
		return amistosos;
	}
	
	public ArrayList<negocio.Partido> getAmistosos(int jugador, int skip, 
			int limit, String toSearch) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		ArrayList<negocio.Solicitud> solicitudes = 
				datos.Solicitud.getInstance().getAllSolicitudesAmistosos(jugador);
		
		String query = "SELECT * FROM partidos WHERE estado=? AND solicitud=?";
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<negocio.Partido>();
		int count = 0;
		for(negocio.Solicitud solicitud : solicitudes) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, negocio.Estado.PARTIDO_FINALIZADO);
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
						if(amistosos.size() <= limit) {
							negocio.Partido amistoso = new negocio.Partido();
							amistoso.setId(rs.getInt(1));
							amistoso.setSolicitud(solicitud);
							amistoso.setFecha(rs.getDate(2));
							amistoso.setResultadoUno(datos.Resultado.getInstance()
									.getOne(solicitud.getJugadorUno(), amistoso));
							amistoso.setResultadoDos(datos.Resultado.getInstance()
									.getOne(solicitud.getJugadorDos(), amistoso));
							amistoso.setRegistro(datos.Usuario.getInstance().getOne(rs.getInt(5)));
							amistosos.add(amistoso);
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
		
		return amistosos;
	}
	
	public negocio.Partido getOne(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM partidos WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Partido partido = new negocio.Partido();
		if(rs.next()) {
			partido.setId(rs.getInt(1));
			partido.setFecha(rs.getDate(2));
			partido.setEstado(datos.Estado.getInstance().getOne(rs.getInt(3)));
			partido.setSolicitud(datos.Solicitud.getInstance().getOne(rs.getInt(4)));
			partido.setRegistro(datos.Usuario.getInstance().getOne(rs.getInt(5)));
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return partido;
	}
}
