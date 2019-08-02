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
	
	public ArrayList<negocio.Partido> getAmistososPendientes(int jugador, int skip, 
			int limit, String toSearch) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT p.id, s.fecha, j_uno.id, j_uno.nombre, j_uno.apodo, "
				+ "j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM partidos AS p "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE (j_uno.id = ? OR j_dos.id = ?) AND p.estado = ? "
				+ "AND s.liga IS NULL "
				+ "AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) "
				+ "OR (j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?))) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador);
		stmt.setInt(2, jugador);
		stmt.setInt(3, negocio.Estado.PARTIDO_PENDIENTE);
		toSearch = "%" + toSearch + "%";
		stmt.setInt(4, jugador);
		stmt.setString(5, toSearch);
		stmt.setString(6, toSearch);
		stmt.setInt(7, jugador);
		stmt.setString(8, toSearch);
		stmt.setString(9, toSearch);
		stmt.setInt(10, skip);
		stmt.setInt(11, limit);
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Partido amistoso = new negocio.Partido();
			amistoso.setId(rs.getInt(1));
			
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setFecha(rs.getDate(2));
			
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			solicitud.setJugadorUno(jugadorUno);
			
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			solicitud.setJugadorDos(jugadorDos);
			
			amistoso.setSolicitud(solicitud);
			
			amistosos.add(amistoso);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return amistosos;
	}
	
	public ArrayList<negocio.Partido> getAmistososPendientes(int jugador, int skip, int limit) 
			throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT p.id, s.fecha, j_uno.id, j_uno.nombre, j_uno.apodo, "
				+ "j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM partidos AS p "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE (j_uno.id = ? OR j_dos.id = ?) AND p.estado = ? "
				+ "AND s.liga IS NULL LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador);
		stmt.setInt(2, jugador);
		stmt.setInt(3, negocio.Estado.PARTIDO_PENDIENTE);
		stmt.setInt(4, skip);
		stmt.setInt(5, limit);
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Partido amistoso = new negocio.Partido();
			amistoso.setId(rs.getInt(1));
			
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setFecha(rs.getDate(2));
			
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			solicitud.setJugadorUno(jugadorUno);
			
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			solicitud.setJugadorDos(jugadorDos);
			
			amistoso.setSolicitud(solicitud);
			
			amistosos.add(amistoso);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return amistosos;
	}
	
	public ArrayList<negocio.Partido> getAmistosos(int jugador, int skip, 
			int limit) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT p.id, p.fecha, j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, "
				+ "j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles, u_reg.id, "
				+ "u_reg.nombre, u_reg.apodo FROM partidos AS p "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN resultados AS r_uno ON r_uno.id_partido = p.id "
				+ "INNER JOIN resultados AS r_dos ON r_dos.id_partido = p.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = r_uno.id_jugador "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = r_dos.id_jugador "
				+ "INNER JOIN usuarios AS u_reg ON u_reg.id = p.registro "
				+ "WHERE r_uno.id_jugador = ? AND r_dos.id_jugador != ? "
				+ "AND p.estado = ? "
				+ "AND s.liga IS NULL LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador);
		stmt.setInt(2, jugador);
		stmt.setInt(3, negocio.Estado.PARTIDO_FINALIZADO);
		stmt.setInt(4, skip);
		stmt.setInt(5, limit);
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<>();
		
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(1));
			partido.setFecha(rs.getDate(2));
			
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setJugador(jugadorUno);
			resultadoUno.setGoles(rs.getInt(9));
			partido.setResultadoUno(resultadoUno);
			
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setJugador(jugadorDos);
			resultadoDos.setGoles(rs.getInt(10));
			partido.setResultadoDos(resultadoDos);
			
			negocio.Usuario registro = new negocio.Usuario();
			registro.setId(rs.getInt(11));
			registro.setNombre(rs.getString(12));
			registro.setApodo(rs.getString(13));
			partido.setRegistro(registro);
			
			amistosos.add(partido);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return amistosos;
	}
	
	public ArrayList<negocio.Partido> getAmistosos(int jugador, int skip, 
			int limit, String toSearch) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT p.id, p.fecha, j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, "
				+ "j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles, u_reg.id, "
				+ "u_reg.nombre, u_reg.apodo FROM partidos AS p "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN resultados AS r_uno ON r_uno.id_partido = p.id "
				+ "INNER JOIN resultados AS r_dos ON r_dos.id_partido = p.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = r_uno.id_jugador "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = r_dos.id_jugador "
				+ "INNER JOIN usuarios AS u_reg ON u_reg.id = p.registro "
				+ "WHERE r_uno.id_jugador = ? AND r_dos.id_jugador != ? "
				+ "AND p.estado = ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?) "
				+ "AND s.liga IS NULL LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador);
		stmt.setInt(2, jugador);
		stmt.setInt(3, negocio.Estado.PARTIDO_FINALIZADO);
		toSearch = "%" + toSearch + "%";
		stmt.setString(4, toSearch);
		stmt.setString(5, toSearch);
		stmt.setInt(6, skip);
		stmt.setInt(7, limit);
		
		ArrayList<negocio.Partido> amistosos = new ArrayList<>();
		
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(1));
			partido.setFecha(rs.getDate(2));
			
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setJugador(jugadorUno);
			resultadoUno.setGoles(rs.getInt(9));
			partido.setResultadoUno(resultadoUno);
			
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setJugador(jugadorDos);
			resultadoDos.setGoles(rs.getInt(10));
			partido.setResultadoDos(resultadoDos);
			
			negocio.Usuario registro = new negocio.Usuario();
			registro.setId(rs.getInt(11));
			registro.setNombre(rs.getString(12));
			registro.setNombre(rs.getString(13));
			partido.setRegistro(registro);
			
			amistosos.add(partido);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return amistosos;
	}
	
	public negocio.Partido getOne(int id) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT p.id, j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, "
				+ "j_dos.nombre, j_dos.apodo, s.liga FROM partidos AS p "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE p.id = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		negocio.Partido partido = new negocio.Partido();
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			partido.setId(rs.getInt(1));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(2));
			jugadorUno.setNombre(rs.getString(3));
			jugadorUno.setApodo(rs.getString(4));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(5));
			jugadorDos.setNombre(rs.getString(6));
			jugadorDos.setApodo(rs.getString(7));
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			//TODO: incorporar liga
			solicitud.setLiga(null);
			
			partido.setSolicitud(solicitud);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return partido;
	}
}
