package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import negocio.Estado;

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
		
		String query = "SELECT * FROM solicitudes WHERE estado=? AND jugador_dos=? "
				+ "AND liga IS NULL LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorDos.getId());
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Usuario jugadorUno = datos.Usuario.getInstance().getOne(rs.getInt(5));
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
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
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_dos=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorDos.getId());
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Usuario jugadorUno = datos.Usuario.getInstance().getOne(rs.getInt(5));
			if(jugadorUno.getNombre().contains(toSearch) || jugadorUno.getApodo().contains(toSearch))
			{
				negocio.Solicitud solicitud = new negocio.Solicitud();
				solicitud.setId(rs.getInt(1));
				solicitud.setFecha(rs.getDate(2));
				solicitud.setVencimiento(rs.getDate(3));
				solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
				solicitud.setJugadorUno(jugadorUno);
				solicitud.setJugadorDos(jugadorDos);
				solicitudes.add(solicitud);
			}
		}
		
		ArrayList<negocio.Solicitud> filteredSolicitudes = new ArrayList<>();
		
		if(skip+limit < solicitudes.size()) { 
			for(int i=skip; i<skip+limit; ++i)
			{
				filteredSolicitudes.add(solicitudes.get(i));
			}
		} else {
			for(int i=skip; i<solicitudes.size(); ++i) {
				filteredSolicitudes.add(solicitudes.get(i));
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return filteredSolicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno, int skip, int limit) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_uno=? "
				+ "AND liga IS NULL LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorUno.getId());
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Usuario jugadorDos = datos.Usuario.getInstance().getOne(rs.getInt(6));
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
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
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_uno=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorUno.getId());
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Usuario jugadorDos = datos.Usuario.getInstance().getOne(rs.getInt(6));
			if(jugadorDos.getNombre().contains(toSearch) || jugadorDos.getApodo().contains(toSearch))
			{
				negocio.Solicitud solicitud = new negocio.Solicitud();
				solicitud.setId(rs.getInt(1));
				solicitud.setFecha(rs.getDate(2));
				solicitud.setVencimiento(rs.getDate(3));
				solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
				solicitud.setJugadorUno(jugadorUno);
				solicitud.setJugadorDos(jugadorDos);
				solicitudes.add(solicitud);
			}
		}
		
		ArrayList<negocio.Solicitud> filteredSolicitudes = new ArrayList<>();
		
		if(skip+limit < solicitudes.size()) { 
			for(int i=skip; i<skip+limit; ++i)
			{
				filteredSolicitudes.add(solicitudes.get(i));
			}
		} else {
			for(int i=skip; i<solicitudes.size(); ++i) {
				filteredSolicitudes.add(solicitudes.get(i));
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return filteredSolicitudes;
	}
	
	public int getCountSolicitudesEnviadasAmistososPendientesFiltered
		(negocio.Usuario jugadorUno, String toSearch) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_uno=? "
			+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorUno.getId());
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()) {
			negocio.Usuario jugadorDos = datos.Usuario.getInstance().getOne(rs.getInt(6));
			if(jugadorDos.getNombre().contains(toSearch) || jugadorDos.getApodo().contains(toSearch))
			{
				++count;
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountSolicitudesRecibidasAmistososPendientesFiltered
		(negocio.Usuario jugadorDos, String toSearch) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_dos=? "
			+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorDos.getId());
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()) {
			negocio.Usuario jugadorUno = datos.Usuario.getInstance().getOne(rs.getInt(5));
			if(jugadorUno.getNombre().contains(toSearch) || jugadorUno.getApodo().contains(toSearch))
			{
				++count;
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountSolicitudesEnviadasAmistososPendientes(negocio.Usuario jugadorUno) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from solicitudes WHERE estado=? AND jugador_uno=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorUno.getId());
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public ArrayList<negocio.Solicitud> getAllSolicitudesAmistosos(int jugador) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE (jugador_uno=? OR jugador_dos=?) "
				+ "AND estado=? AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador);
		stmt.setInt(2, jugador);
		stmt.setInt(3, negocio.Estado.SOLICITUD_ACEPTADA);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<negocio.Solicitud>();
		while(rs.next()) {
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			negocio.Usuario jugadorUno = datos.Usuario.getInstance().getOne(rs.getInt(5));
			negocio.Usuario jugadorDos = datos.Usuario.getInstance().getOne(rs.getInt(6));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			solicitudes.add(solicitud);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public int getCountSolicitudesRecibidasAmistososPendientes(negocio.Usuario jugadorDos) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM solicitudes WHERE estado=? AND jugador_dos=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorDos.getId());
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public negocio.Solicitud getOne(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Solicitud solicitud = new negocio.Solicitud();
		if(rs.next()) {
			solicitud.setId(rs.getInt(1));
			solicitud.setFecha(rs.getDate(2));
			solicitud.setVencimiento(rs.getDate(3));
			solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(4)));
			solicitud.setJugadorUno(datos.Usuario.getInstance().getOne(rs.getInt(5)));
			solicitud.setJugadorDos(datos.Usuario.getInstance().getOne(rs.getInt(6)));
			//TODO: agregar liga cuando se implemente
			solicitud.setLiga(null);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitud;
	}

	public ArrayList<negocio.Solicitud> getAllSolicitudesLigaUsuario(int idLiga, int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE (jugador_uno=? OR jugador_dos=?) AND estado=? AND liga=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idUsuario);
		stmt.setInt(3, Estado.SOLICITUD_ACEPTADA);
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

}

