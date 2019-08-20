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
	
	public void createSolicitudLiga(int jugadorUno, int jugadorDos, int liga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO solicitudes(fecha, vencimiento, estado, jugador_uno, "
				+ "jugador_dos, liga) VALUES (?,?,?,?,?,?)";
		
		Calendar c = Calendar.getInstance();
		java.sql.Date today = new java.sql.Date(c.getTime().getTime());
		c.add(Calendar.DATE, negocio.Solicitud.MAX_DIAS_SOLICITUD);
		java.sql.Date vencimiento = new java.sql.Date(c.getTime().getTime());
		
		stmt = conn.prepareStatement(query);
		stmt.setDate(1, today);
		stmt.setDate(2, vencimiento);
		stmt.setInt(3, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(4, jugadorUno);
		stmt.setInt(5, jugadorDos);
		stmt.setInt(6, liga);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
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
		c.add(Calendar.DATE, negocio.Solicitud.MAX_DIAS_SOLICITUD);
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

	public ArrayList<negocio.Solicitud> getAllSolicitudesLigaUsuario(int idLiga, int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE (jugador_uno=? OR jugador_dos=?) "
				+ "AND estado=? AND liga=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idUsuario);
		stmt.setInt(3, negocio.Estado.SOLICITUD_ACEPTADA);
		stmt.setInt(4, idLiga);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<negocio.Solicitud>();
		while(rs.next()) {
			negocio.Solicitud so = new negocio.Solicitud();
			so.setId(rs.getInt(1));
			so.setFecha(rs.getDate(2));
			so.setVencimiento(rs.getDate(3));
			so.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
			so.setJugadorUno(datos.Usuario.getInstance().getOne(rs.getInt(5)));
			so.setJugadorDos(datos.Usuario.getInstance().getOne(rs.getInt(6)));
			so.setLiga(datos.Liga.getInstance().getOne(rs.getInt(7)));
			solicitudes.add(so);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}

	public negocio.Solicitud getOne(int idSolicitud) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idSolicitud);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Solicitud solicitud = null;
		if(rs.next()) {
			solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
			solicitud.setJugadorUno(datos.Usuario.getInstance().getOne(rs.getInt(5)));
			solicitud.setJugadorDos(datos.Usuario.getInstance().getOne(rs.getInt(6)));
			solicitud.setLiga(datos.Liga.getInstance().getOne(rs.getInt(7)));
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitud;
	}
	
	public boolean checkSolicitudLiga(int idUno, int idDos, int idLiga) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE "
				+ "((jugador_uno = ? AND jugador_dos = ?) OR (jugador_uno = ? AND jugador_dos = ?)) "
				+ "AND liga = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUno);
		stmt.setInt(2, idDos);
		stmt.setInt(3, idDos);
		stmt.setInt(4, idUno);
		stmt.setInt(5, idLiga);
		
		ResultSet rs = stmt.executeQuery();
		boolean result = rs.next();
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return result;
	}
	
	public int getCountSolicitudesEnviadasLigaPendientes(int id)
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes WHERE (jugador_uno = ? OR jugador_dos = ?) "
				+ "AND estado = ? AND liga IS NOT NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.SOLICITUD_PENDIENTE);
		
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
	
	public int getCountSolicitudesEnviadasLigaPendientes(int id, String search)
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes AS s "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE (s.jugador_uno = ? OR s.jugador_dos = ?) "
				+ "AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) OR "
				+ "(j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?))) "
				+ "AND s.estado = ? AND s.liga IS NOT NULL";
		
		stmt = conn.prepareStatement(query);
		search = "%" + search + "%";
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setString(4, search);
		stmt.setString(5, search);
		stmt.setInt(6, id);
		stmt.setString(7, search);
		stmt.setString(8, search);
		stmt.setInt(9, negocio.Estado.SOLICITUD_PENDIENTE);
		
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
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasLigaPendientes(int id, int skip, 
			int limit) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, l.nombre, l.temporada, j_uno.id, "
				+ "j_uno.nombre, j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM solicitudes AS s "
				+ "INNER JOIN ligas AS l ON s.liga = l.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE s.estado = ? AND ((j_uno.id = ? AND j_dos.id != ?) OR "
				+ "(j_uno.id != ? AND j_dos.id = ?)) LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, id);
		stmt.setInt(5, id);
		stmt.setInt(6, skip);
		stmt.setInt(7, limit);
		
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Liga liga = new negocio.Liga();
			liga.setNombre(rs.getString(4));
			liga.setTemporada(rs.getInt(5));
			solicitud.setLiga(liga);
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(6));
			jugadorUno.setNombre(rs.getString(7));
			jugadorUno.setApodo(rs.getString(8));
			solicitud.setJugadorUno(jugadorUno);
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(9));
			jugadorDos.setNombre(rs.getString(10));
			jugadorDos.setApodo(rs.getString(11));
			solicitud.setJugadorDos(jugadorDos);
			
			solicitudes.add(solicitud);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasLigaPendientes(int id, int skip, 
			int limit, String search) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT s.id, s.fecha, s.vencimiento, l.nombre, l.temporada, j_uno.id, "
				+ "j_uno.nombre, j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM solicitudes AS s "
				+ "INNER JOIN ligas AS l ON s.liga = l.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE s.estado = ? AND ((j_uno.id = ? AND j_dos.id != ? "
				+ "AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?)) OR "
				+ "(j_uno.id != ? AND j_dos.id = ? AND (j_uno.nombre LIKE ? "
				+ "OR j_uno.apodo LIKE ?))) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		search = "%" + search + "%";
		stmt.setString(4, search);
		stmt.setString(5, search);
		stmt.setInt(6, id);
		stmt.setInt(7, id);
		stmt.setString(8, search);
		stmt.setString(9, search);
		stmt.setInt(10, skip);
		stmt.setInt(11, limit);
		
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			negocio.Liga liga = new negocio.Liga();
			liga.setNombre(rs.getString(4));
			liga.setTemporada(rs.getInt(5));
			solicitud.setLiga(liga);
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(6));
			jugadorUno.setNombre(rs.getString(7));
			jugadorUno.setApodo(rs.getString(8));
			solicitud.setJugadorUno(jugadorUno);
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(9));
			jugadorDos.setNombre(rs.getString(10));
			jugadorDos.setApodo(rs.getString(11));
			solicitud.setJugadorDos(jugadorDos);
			
			solicitudes.add(solicitud);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
}

