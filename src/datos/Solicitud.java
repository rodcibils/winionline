package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Solicitud {
	private static Solicitud instance = null;
	
	public static Solicitud getInstance() {
		if(instance == null)
			instance = new Solicitud();
		
		return instance;
	}
	
	public void aceptarSolicitud(int idSolicitud) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE solicitudes SET estado=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_ACEPTADA);
		stmt.setInt(2, idSolicitud);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public boolean checkIfExists(int jugadorUno, int jugadorDos) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE jugador_uno=? "
				+ "AND jugador_dos=? AND estado=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorUno);
		stmt.setInt(2, jugadorDos);
		stmt.setInt(3, negocio.Estado.SOLICITUD_PENDIENTE);
		
		ResultSet rs = stmt.executeQuery();
		boolean flag = rs.next();
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return flag;
	}
	
	public void createSolicitudAmistoso(int jugadorUno, int jugadorDos) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO solicitudes(fecha, vencimiento, estado, jugador_uno, "
				+ "jugador_dos) VALUES (?,?,?,?,?)";
		
		Date fecha = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.DATE, 10);
		Date vencimiento = c.getTime();
		
		java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());
		java.sql.Date sqlVencimiento = new java.sql.Date(vencimiento.getTime());
		
		stmt = conn.prepareStatement(query);
		stmt.setDate(1, sqlFecha);
		stmt.setDate(2, sqlVencimiento);
		stmt.setInt(3, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(4, jugadorUno);
		stmt.setInt(5, jugadorDos);
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void cleanupSolicitudes(int idJugador) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "DELETE FROM solicitudes WHERE jugador_uno=? OR jugador_dos=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idJugador);
		stmt.setInt(2, idJugador);
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void delete(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "DELETE FROM solicitudes WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesRecibidasAmistososPendientes
		(negocio.Usuario jugadorDos, int skip, int limit) throws ClassNotFoundException, 
		SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, u.id, u.nombre, "
				+ "u.apodo FROM solicitudes AS s INNER JOIN usuarios AS u "
				+ "ON u.id = s.jugador_uno WHERE s.jugador_dos = ? AND s.estado = ? "
				+ "AND s.liga IS NULL "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorDos.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(4));
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			solicitudes.add(solicitud);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesRecibidasAmistososPendientes
		(negocio.Usuario jugadorDos, int skip, int limit, String toSearch) 
		throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, u.id, u.nombre, "
				+ "u.apodo FROM solicitudes AS s INNER JOIN usuarios AS u "
				+ "ON u.id = s.jugador_uno WHERE s.jugador_dos = ? AND s.estado = ? "
				+ "AND (u.nombre LIKE ? OR u.apodo LIKE ?) "
				+ "AND s.liga IS NULL "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorDos.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		toSearch = "%" + toSearch + "%";
		stmt.setString(3, toSearch);
		stmt.setString(4, toSearch);
		stmt.setInt(5, skip);
		stmt.setInt(6, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(4));
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			solicitudes.add(solicitud);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno, int skip, int limit) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, u.id, u.nombre, "
				+ "u.apodo FROM solicitudes AS s INNER JOIN usuarios AS u "
				+ "ON u.id = s.jugador_dos WHERE s.jugador_uno = ? AND s.estado = ? "
				+ "AND s.liga IS NULL "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorUno.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(4));
			jugadorDos.setNombre(rs.getString(5));
			jugadorDos.setApodo(rs.getString(6));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			solicitudes.add(solicitud);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno, int skip, int limit, String toSearch) 
		throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, u.id, u.nombre, "
				+ "u.apodo FROM solicitudes AS s INNER JOIN usuarios AS u "
				+ "ON u.id = s.jugador_dos WHERE s.jugador_uno = ? AND s.estado = ? "
				+ "AND (u.nombre LIKE ? OR u.apodo LIKE ?) "
				+ "AND s.liga IS NULL "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorUno.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		toSearch = "%" + toSearch + "%";
		stmt.setString(3, toSearch);
		stmt.setString(4, toSearch);
		stmt.setInt(5, skip);
		stmt.setInt(6, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(4));
			jugadorDos.setNombre(rs.getString(5));
			jugadorDos.setApodo(rs.getString(6));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			solicitudes.add(solicitud);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public int getCountSolicitudesRecibidasAmistososPendientes
		(negocio.Usuario jugadorDos) throws ClassNotFoundException, 
		SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes WHERE jugador_dos = ? "
				+ "AND estado = ? AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorDos.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		
		int count = 0;
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountSolicitudesRecibidasAmistososPendientes
		(negocio.Usuario jugadorDos, String toSearch) throws ClassNotFoundException, 
		SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes AS s "
				+ "INNER JOIN usuarios AS u ON s.jugador_uno = u.id "
				+ "WHERE s.jugador_dos = ? AND (u.nombre LIKE ? OR u.apodo LIKE ?) "
				+ "AND s.estado = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorDos.getId());
		toSearch = "%" + toSearch + "%";
		stmt.setString(2, toSearch);
		stmt.setString(3, toSearch);
		stmt.setInt(4, negocio.Estado.SOLICITUD_PENDIENTE);
		
		int count = 0;
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno) throws ClassNotFoundException, 
		SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes WHERE jugador_uno = ? "
				+ "AND estado = ? AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorUno.getId());
		stmt.setInt(2, negocio.Estado.SOLICITUD_PENDIENTE);
		
		int count = 0;
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno, String toSearch) throws ClassNotFoundException, 
		SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes AS s "
				+ "INNER JOIN usuarios AS u ON s.jugador_dos = u.id "
				+ "WHERE s.jugador_uno = ? AND (u.nombre LIKE ? OR u.apodo LIKE ?) "
				+ "AND s.estado = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugadorUno.getId());
		toSearch = "%" + toSearch + "%";
		stmt.setString(2, toSearch);
		stmt.setString(3, toSearch);
		stmt.setInt(4, negocio.Estado.SOLICITUD_PENDIENTE);
		
		int count = 0;
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);
		}
	
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
}

