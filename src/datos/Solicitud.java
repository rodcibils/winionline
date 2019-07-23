package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solicitud {
	private static Solicitud instance = null;
	
	public static Solicitud getInstance() {
		if(instance == null)
			instance = new Solicitud();
		
		return instance;
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
}

