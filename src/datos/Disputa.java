package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class Disputa 
{
	private static Disputa instance = null;
	
	public static Disputa getInstance() {
		if(instance == null) {
			instance = new Disputa();
		}
		
		return instance;
	}
	
	public int getCountDisputasCerradas(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "WHERE d.estado = ? AND (s.jugador_uno = ? OR s.jugador_dos = ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_CERRADA);
		stmt.setInt(2, id);
		stmt.setInt(3, id);

		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountDisputasCerradas(int id, String search) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE d.estado = ? AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? "
				+ "OR j_uno.apodo LIKE ?)) OR (j_dos.id != ? AND (j_dos.nombre LIKE ? "
				+ "OR j_dos.apodo LIKE ?)))";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_CERRADA);
		stmt.setInt(2, id);
		search = "%" + search + "%";
		stmt.setString(3, search);
		stmt.setString(4, search);
		stmt.setInt(5, id);
		stmt.setString(6, search);
		stmt.setString(7, search);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public ArrayList<negocio.Disputa> getDisputasCerradas(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.id_partido, d.vencimiento, j_uno.id, j_uno.nombre, j_uno.apodo, "
				+ "j_dos.id, j_dos.nombre, j_dos.apodo FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE d.estado = ? AND (j_uno.id = ? OR j_dos.id = ?) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_CERRADA);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, skip);
		stmt.setInt(5, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(1));
			disputa.setVencimiento(rs.getDate(2));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			partido.setSolicitud(solicitud);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public ArrayList<negocio.Disputa> getDisputasCerradas(int id, int skip, int limit, 
			String search) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.id_partido, d.vencimiento, j_uno.id, j_uno.nombre, j_uno.apodo, "
				+ "j_dos.id, j_dos.nombre, j_dos.apodo FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE d.estado = ? AND (j_uno.id = ? OR j_dos.id = ?) "
				+ "AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) "
				+ "OR (j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.nombre LIKE ?))) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_CERRADA);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, id);
		search = "%" + search + "%";
		stmt.setString(5, search);
		stmt.setString(6, search);
		stmt.setInt(7, id);
		stmt.setString(8, search);
		stmt.setString(9, search);
		stmt.setInt(10, skip);
		stmt.setInt(11, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(1));
			disputa.setVencimiento(rs.getDate(2));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(3));
			jugadorUno.setNombre(rs.getString(4));
			jugadorUno.setApodo(rs.getString(5));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(6));
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			negocio.Solicitud solicitud = new negocio.Solicitud();
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			partido.setSolicitud(solicitud);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public void cerrarDisputa(int idDisputa) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE disputas SET estado = ? WHERE id_partido = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_CERRADA);
		stmt.setInt(2, idDisputa);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getVotos(int idDisputa, int idJugador) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_disputa WHERE "
				+ "id_disputa = ? AND id_voto = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idDisputa);
		stmt.setInt(2, idJugador);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public ArrayList<negocio.Disputa> getDisputasVencidas() 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.id_partido, j_uno.id, j_dos.id FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE d.estado = ? AND d.vencimiento < CURRENT_DATE()";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_EN_CURSO);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			negocio.Partido partido = new negocio.Partido();
			negocio.Solicitud solicitud = new negocio.Solicitud();
			negocio.Usuario jugadorUno = new negocio.Usuario();
			negocio.Usuario jugadorDos = new negocio.Usuario();
			partido.setId(rs.getInt(1));
			jugadorUno.setId(rs.getInt(2));
			jugadorDos.setId(rs.getInt(3));
			solicitud.setJugadorUno(jugadorUno);
			solicitud.setJugadorDos(jugadorDos);
			partido.setSolicitud(solicitud);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public int getCountDisputasVencidas() throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas WHERE "
				+ "vencimiento < CURRENT_DATE() AND estado = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_EN_CURSO);
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public void votarDisputa(int idVotante, int idDisputa, int idVoto) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO usuario_disputa(id_usuario, id_disputa, id_voto) "
				+ "VALUES (?,?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idVotante);
		stmt.setInt(2, idDisputa);
		stmt.setInt(3, idVoto);
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getAllCount(int id, String toSearch) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON s.jugador_uno = j_uno.id "
				+ "INNER JOIN usuarios AS j_dos ON s.jugador_dos = j_dos.id "
				+ "WHERE j_uno.id != ? AND j_dos.id != ? AND d.estado = ? "
				+ "AND ((j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?) OR (j_dos.nombre LIKE ? "
				+ "OR j_dos.apodo LIKE ?)) AND p.id NOT IN (SELECT id_disputa FROM "
				+ "usuario_disputa WHERE id_usuario = ?) AND d.vencimiento >= CURRENT_DATE()";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.DISPUTA_EN_CURSO);
		toSearch = "%" + toSearch + "%";
		stmt.setString(4, toSearch);
		stmt.setString(5, toSearch);
		stmt.setString(6, toSearch);
		stmt.setString(7, toSearch);
		stmt.setInt(8, id);
		
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
	
	public int getAllCount(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON s.jugador_uno = j_uno.id "
				+ "INNER JOIN usuarios AS j_dos ON s.jugador_dos = j_dos.id "
				+ "WHERE j_uno.id != ? AND j_dos.id != ? AND d.estado = ? AND p.id "
				+ "NOT IN (SELECT id_disputa FROM usuario_disputa WHERE id_usuario = ?) "
				+ "AND d.vencimiento >= CURRENT_DATE()";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.DISPUTA_EN_CURSO);
		stmt.setInt(4, id);
		
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
	
	public ArrayList<negocio.Disputa> getAll(int id, int skip, int limit, String toSearch) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.vencimiento, p.id, p.fecha, j_uno.id, j_uno.nombre, j_uno.apodo, "
				+ "j_dos.id, j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles "
				+ "FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON s.jugador_uno = j_uno.id "
				+ "INNER JOIN usuarios AS j_dos ON s.jugador_dos = j_dos.id "
				+ "INNER JOIN resultados AS r_uno ON (r_uno.id_partido = p.id "
				+ "AND r_uno.id_jugador = j_uno.id) "
				+ "INNER JOIN resultados AS r_dos ON (r_dos.id_partido = p.id "
				+ "AND r_dos.id_jugador = j_dos.id) "
				+ "WHERE j_uno.id != ? AND j_dos.id != ? AND d.estado = ? "
				+ "AND ((j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?) OR (j_dos.nombre LIKE ? "
				+ "OR j_dos.apodo LIKE ?)) AND p.id NOT IN (SELECT id_disputa FROM usuario_disputa "
				+ "WHERE id_usuario = ?) AND d.vencimiento >= CURRENT_DATE() "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.DISPUTA_EN_CURSO);
		toSearch = "%" + toSearch + "%";
		stmt.setString(4, toSearch);
		stmt.setString(5, toSearch);
		stmt.setString(6, toSearch);
		stmt.setString(7, toSearch);
		stmt.setInt(8, id);
		stmt.setInt(9, skip);
		stmt.setInt(10, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(1));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(2));
			partido.setFecha(rs.getDate(3));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(4));
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(7));
			jugadorDos.setNombre(rs.getString(8));
			jugadorDos.setApodo(rs.getString(9));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setJugador(jugadorUno);
			resultadoUno.setGoles(rs.getInt(10));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setJugador(jugadorDos);
			resultadoDos.setGoles(rs.getInt(11));
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public ArrayList<negocio.Disputa> getAll(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.vencimiento, p.id, p.fecha, j_uno.id, j_uno.nombre, "
				+ "j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles "
				+ "FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON s.jugador_uno = j_uno.id "
				+ "INNER JOIN usuarios AS j_dos ON s.jugador_dos = j_dos.id "
				+ "INNER JOIN resultados AS r_uno ON (r_uno.id_partido = p.id "
				+ "AND r_uno.id_jugador = j_uno.id) "
				+ "INNER JOIN resultados AS r_dos ON (r_dos.id_partido = p.id "
				+ "AND r_dos.id_jugador = j_dos.id) "
				+ "WHERE j_uno.id != ? AND j_dos.id != ? AND d.estado = ? AND p.id NOT IN "
				+ "(SELECT id_disputa FROM usuario_disputa WHERE id_usuario = ?) "
				+ "AND d.vencimiento >= CURRENT_DATE() "
				+ "LIMIT ?,? ";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.DISPUTA_EN_CURSO);
		stmt.setInt(4, id);
		stmt.setInt(5, skip);
		stmt.setInt(6, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(1));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(2));
			partido.setFecha(rs.getDate(3));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(4));
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(7));
			jugadorDos.setNombre(rs.getString(8));
			jugadorDos.setApodo(rs.getString(9));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setJugador(jugadorUno);
			resultadoUno.setGoles(rs.getInt(10));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setJugador(jugadorDos);
			resultadoDos.setGoles(rs.getInt(11));
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public negocio.Disputa getOne(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.fecha, d.vencimiento, p.fecha, j_uno.id, j_uno.nombre, "
				+ "j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo, r_uno.goles, "
				+ "r_dos.goles FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "INNER JOIN resultados AS r_uno ON "
				+ "(p.id = r_uno.id_partido AND s.jugador_uno = r_uno.id_jugador) "
				+ "INNER JOIN resultados AS r_dos ON "
				+ "(p.id = r_dos.id_partido AND s.jugador_dos = r_dos.id_jugador) "
				+ "WHERE d.id_partido = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Disputa disputa = new negocio.Disputa();
		if(rs.next()) {
			disputa.setFecha(rs.getDate(1));
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setFecha(rs.getDate(3));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(4));
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(7));
			jugadorDos.setNombre(rs.getString(8));
			jugadorDos.setApodo(rs.getString(9));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(10));
			resultadoUno.setJugador(jugadorUno);
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(11));
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputa;
	}
	
	public void insert(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO disputas(id_partido, fecha, vencimiento, "
				+ "estado) VALUES (?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		Calendar c = Calendar.getInstance();
		java.sql.Date sqlToday = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(2, sqlToday);
		c.add(Calendar.DATE, negocio.Disputa.MAX_DIAS_DISPUTA);
		java.sql.Date sqlVencimiento = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(3, sqlVencimiento);
		stmt.setInt(4, negocio.Estado.DISPUTA_EN_CURSO);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getCountDisputasEnCurso(int idUsuario) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "WHERE (s.jugador_uno = ? OR s.jugador_dos = ?) AND d.estado = ? "
				+ "AND d.vencimiento >= CURRENT_DATE()";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idUsuario);
		stmt.setInt(3, negocio.Estado.DISPUTA_EN_CURSO);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public int getCountDisputasEnCurso(int idUsuario, String search) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE (s.jugador_uno = ? OR s.jugador_dos = ?) "
				+ "AND (j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?) "
				+ "OR (j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?))) "
				+ "AND d.estado = ? AND d.vencimiento >= CURRENT_DATE()";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idUsuario);
		stmt.setInt(3, idUsuario);
		search = "%" + search + "%";
		stmt.setString(4, search);
		stmt.setString(5, search);
		stmt.setInt(6, idUsuario);
		stmt.setString(7, search);
		stmt.setString(8, search);
		stmt.setInt(9, negocio.Estado.DISPUTA_EN_CURSO);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
	
	public ArrayList<negocio.Disputa> getDisputasEnCurso(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.fecha, d.vencimiento, p.id, p.fecha, j_uno.nombre, "
				+ "j_uno.apodo, j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles "
				+ "FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN resultados AS r_uno ON r_uno.id_partido = p.id "
				+ "AND s.jugador_uno = r_uno.id_jugador "
				+ "INNER JOIN resultados AS r_dos ON r_dos.id_partido = p.id "
				+ "AND s.jugador_dos = r_dos.id_jugador "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE d.estado = ? AND ((r_uno.id_jugador = ? AND r_dos.id_jugador != ?) OR "
				+ "(r_uno.id_jugador != ? AND r_dos.id_jugador = ?)) "
				+ "AND d.vencimiento >= CURRENT_DATE() "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_EN_CURSO);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, id);
		stmt.setInt(5, id);
		stmt.setInt(6, skip);
		stmt.setInt(7, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setFecha(rs.getDate(1));
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			partido.setFecha(rs.getDate(4));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(9));
			resultadoUno.setJugador(jugadorUno);
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(10));
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
	
	public ArrayList<negocio.Disputa> getDisputasEnCurso(int id, int skip, 
			int limit, String search) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT d.fecha, d.vencimiento, p.id, p.fecha, j_uno.nombre, "
				+ "j_uno.apodo, j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles "
				+ "FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN resultados AS r_uno ON (r_uno.id_partido = p.id "
				+ "AND r_uno.id_jugador = s.jugador_uno) "
				+ "INNER JOIN resultados AS r_dos ON (r_dos.id_partido = p.id AND "
				+ "r_dos.id_jugador = s.jugador_dos) "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = r_uno.id_jugador "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = r_dos.id_jugador "
				+ "WHERE d.estado = ? AND ((r_uno.id_jugador = ? AND r_dos.id_jugador != ?) OR "
				+ "(r_uno.id_jugador != ? AND r_dos.id_jugador = ?)) "
				+ "AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) "
				+ "OR (j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?))) "
				+ "AND d.vencimiento >= CURRENT_DATE() "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.DISPUTA_EN_CURSO);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, id);
		stmt.setInt(5, id);
		stmt.setInt(6, id);
		search = "%" + search + "%";
		stmt.setString(7, search);
		stmt.setString(8, search);
		stmt.setInt(9, id);
		stmt.setString(10, search);
		stmt.setString(11, search);
		stmt.setInt(12, skip);
		stmt.setInt(13, limit);
		
		ArrayList<negocio.Disputa> disputas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setFecha(rs.getDate(1));
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			partido.setFecha(rs.getDate(4));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setNombre(rs.getString(5));
			jugadorUno.setApodo(rs.getString(6));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setNombre(rs.getString(7));
			jugadorDos.setApodo(rs.getString(8));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(9));
			resultadoUno.setJugador(jugadorUno);
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(10));
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			
			disputas.add(disputa);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return disputas;
	}
}
