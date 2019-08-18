package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class Apelacion {
	private static Apelacion instance = null;
	
	public static Apelacion getInstance() {
		if(instance == null) instance = new Apelacion();
		
		return instance;
	}
	
	public void votarApelacion(int idUsuario, int idApelacion, int idVoto) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE usuario_apelacion SET voto = ? WHERE id_usuario = ? "
				+ "AND id_disputa = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idVoto);
		stmt.setInt(2, idUsuario);
		stmt.setInt(3, idApelacion);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public ArrayList<negocio.Apelacion> getApelacionesAJuzgar(int id, int skip, int limit,
			String search) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT a.fecha, d.vencimiento, p.id, p.fecha, " + 
				"j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, " + 
				"j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles " + 
				"FROM usuario_apelacion AS ua " + 
				"INNER JOIN apelaciones AS a ON a.id_disputa = ua.id_disputa " + 
				"INNER JOIN disputas AS d ON a.id_disputa = d.id_partido " + 
				"INNER JOIN partidos AS p ON p.id = d.id_partido " + 
				"INNER JOIN solicitudes AS s ON p.solicitud = s.id " + 
				"INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno " + 
				"INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos " + 
				"INNER JOIN resultados AS r_uno ON r_uno.id_partido = p.id " + 
				"INNER JOIN resultados AS r_dos ON r_dos.id_partido = p.id " + 
				"WHERE ua.id_usuario = ? AND a.estado = ? AND ua.voto IS NULL " + 
				"AND r_uno.id_jugador = s.jugador_uno AND r_dos.id_jugador = s.jugador_dos " +
				"AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ? OR j_dos.nombre LIKE ? OR " +
				"j_dos.apodo LIKE ?) " +
				"LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, negocio.Estado.APELACION_EN_CURSO);
		search = "%" + search + "%";
		stmt.setString(3, search);
		stmt.setString(4, search);
		stmt.setString(5, search);
		stmt.setString(6, search);
		stmt.setInt(7, skip);
		stmt.setInt(8, limit);
		
		ArrayList<negocio.Apelacion> apelaciones = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Apelacion apelacion = new negocio.Apelacion();
			apelacion.setFecha(rs.getDate(1));
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			partido.setFecha(rs.getDate(4));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(5));
			jugadorUno.setNombre(rs.getString(6));
			jugadorUno.setApodo(rs.getString(7));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(8));
			jugadorDos.setNombre(rs.getString(9));
			jugadorDos.setApodo(rs.getString(10));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(11));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(12));
			resultadoUno.setJugador(jugadorUno);
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			apelacion.setDisputa(disputa);
			
			apelaciones.add(apelacion);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return apelaciones;
	}
	
	public ArrayList<negocio.Apelacion> getApelacionesAJuzgar(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT a.fecha, d.vencimiento, p.id, p.fecha, " + 
				"j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, " + 
				"j_dos.nombre, j_dos.apodo, r_uno.goles, r_dos.goles " + 
				"FROM usuario_apelacion AS ua " + 
				"INNER JOIN apelaciones AS a ON a.id_disputa = ua.id_disputa " + 
				"INNER JOIN disputas AS d ON a.id_disputa = d.id_partido " + 
				"INNER JOIN partidos AS p ON p.id = d.id_partido " + 
				"INNER JOIN solicitudes AS s ON p.solicitud = s.id " + 
				"INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno " + 
				"INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos " + 
				"INNER JOIN resultados AS r_uno ON r_uno.id_partido = p.id " + 
				"INNER JOIN resultados AS r_dos ON r_dos.id_partido = p.id " + 
				"WHERE ua.id_usuario = ? AND a.estado = ? AND ua.voto IS NULL " + 
				"AND r_uno.id_jugador = s.jugador_uno AND r_dos.id_jugador = s.jugador_dos " +
				"LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, negocio.Estado.APELACION_EN_CURSO);
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ArrayList<negocio.Apelacion> apelaciones = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Apelacion apelacion = new negocio.Apelacion();
			apelacion.setFecha(rs.getDate(1));
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			partido.setFecha(rs.getDate(4));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(5));
			jugadorUno.setNombre(rs.getString(6));
			jugadorUno.setApodo(rs.getString(7));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(8));
			jugadorDos.setNombre(rs.getString(9));
			jugadorDos.setApodo(rs.getString(10));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(11));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(12));
			resultadoUno.setJugador(jugadorUno);
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			apelacion.setDisputa(disputa);
			
			apelaciones.add(apelacion);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return apelaciones;
	}
	
	public int getCountApelacionesAJuzgar(int id, String search) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_apelacion AS ua "
				+ "INNER JOIN disputas AS d ON d.id_partido = ua.id_disputa "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE id_usuario = ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ? "
				+ "OR j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?) AND ua.voto IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		search = "%" + search + "%";
		stmt.setString(2, search);
		stmt.setString(3, search);
		stmt.setString(4, search);
		stmt.setString(5, search);
		
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
	
	public void cerrar(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE apelaciones SET estado = ?, cierre = ? WHERE id_disputa = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.APELACION_CERRADA);
		Calendar c = Calendar.getInstance();
		java.sql.Date today = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(2, today);
		stmt.setInt(3, id);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getCountVotos(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_apelacion WHERE id_disputa = ? "
				+ "AND voto IS NOT NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
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
	
	public int getCountApelacionesAJuzgar(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_apelacion WHERE id_usuario = ? "
				+ "AND voto IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
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
	
	public int getVotos(int idApelacion, int idJugador) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_apelacion WHERE "
				+ "id_disputa = ? AND voto = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idApelacion);
		stmt.setInt(2, idJugador);
		
		int votos = 0;
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			votos = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return votos;
	}
	
	public ArrayList<negocio.Usuario> getJugadores(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT j_uno.id, j_dos.id FROM apelaciones AS a "
				+ "INNER JOIN partidos AS p ON a.id_disputa = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE a.id_disputa = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ArrayList<negocio.Usuario> jugadores = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(1));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(2));
			jugadores.add(jugadorUno);
			jugadores.add(jugadorDos);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return jugadores;
	}
	
	public ArrayList<negocio.Apelacion> getMisApelacionesEnCurso(int id, int skip, 
			int limit, String search) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT a.fecha, d.vencimiento, p.id, r_uno.goles, r_dos.goles, "
				+ "j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM apelaciones AS a "
				+ "INNER JOIN disputas AS d ON d.id_partido = a.id_disputa "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN resultados AS r_uno ON r_uno.id_partido = a.id_disputa "
				+ "INNER JOIN resultados AS r_dos ON r_dos.id_partido = a.id_disputa "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = r_uno.id_jugador "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = r_dos.id_jugador "
				+ "WHERE (j_uno.id = ? AND j_dos.id != ?) AND a.estado = ? "
				+ "AND ((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) OR "
				+ "(j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?))) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.APELACION_EN_CURSO);
		stmt.setInt(4, id);
		search = "%" + search + "%";
		stmt.setString(5, search);
		stmt.setString(6, search);
		stmt.setInt(7, id);
		stmt.setString(8, search);
		stmt.setString(9, search);
		stmt.setInt(10, skip);
		stmt.setInt(11, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Apelacion> apelaciones = new ArrayList<>();
		while(rs.next()) {
			negocio.Apelacion apelacion = new negocio.Apelacion();
			apelacion.setFecha(rs.getDate(1));
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(4));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(5));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(6));
			jugadorUno.setNombre(rs.getString(7));
			jugadorUno.setApodo(rs.getString(8));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(9));
			jugadorDos.setNombre(rs.getString(10));
			jugadorDos.setApodo(rs.getString(11));
			resultadoUno.setJugador(jugadorUno);
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			apelacion.setDisputa(disputa);
			
			apelaciones.add(apelacion);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return apelaciones;
	}
	
	public ArrayList<negocio.Apelacion> getMisApelacionesEnCurso(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT a.fecha, d.vencimiento, p.id, r_uno.goles, r_dos.goles, "
				+ "j_uno.id, j_uno.nombre, j_uno.apodo, j_dos.id, j_dos.nombre, j_dos.apodo "
				+ "FROM apelaciones AS a "
				+ "INNER JOIN disputas AS d ON d.id_partido = a.id_disputa "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN resultados AS r_uno ON r_uno.id_partido = a.id_disputa "
				+ "INNER JOIN resultados AS r_dos ON r_dos.id_partido = a.id_disputa "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = r_uno.id_jugador "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = r_dos.id_jugador "
				+ "WHERE (j_uno.id = ? AND j_dos.id != ?) "
				+ "AND a.estado = ? LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, id);
		stmt.setInt(3, negocio.Estado.APELACION_EN_CURSO);
		stmt.setInt(4, skip);
		stmt.setInt(5, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Apelacion> apelaciones = new ArrayList<>();
		while(rs.next()) {
			negocio.Apelacion apelacion = new negocio.Apelacion();
			apelacion.setFecha(rs.getDate(1));
			negocio.Disputa disputa = new negocio.Disputa();
			disputa.setVencimiento(rs.getDate(2));
			negocio.Partido partido = new negocio.Partido();
			partido.setId(rs.getInt(3));
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setGoles(rs.getInt(4));
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setGoles(rs.getInt(5));
			negocio.Usuario jugadorUno = new negocio.Usuario();
			jugadorUno.setId(rs.getInt(6));
			jugadorUno.setNombre(rs.getString(7));
			jugadorUno.setApodo(rs.getString(8));
			negocio.Usuario jugadorDos = new negocio.Usuario();
			jugadorDos.setId(rs.getInt(9));
			jugadorDos.setNombre(rs.getString(10));
			jugadorDos.setApodo(rs.getString(11));
			resultadoUno.setJugador(jugadorUno);
			resultadoDos.setJugador(jugadorDos);
			partido.setResultadoUno(resultadoUno);
			partido.setResultadoDos(resultadoDos);
			disputa.setPartido(partido);
			apelacion.setDisputa(disputa);
			
			apelaciones.add(apelacion);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return apelaciones;
	}
	
	public int getCountMisApelacionesEnCurso(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM apelaciones AS a "
				+ "INNER JOIN partidos AS p ON a.id_disputa = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE a.estado = ? AND (j_uno.id = ? OR j_dos.id = ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.APELACION_EN_CURSO);
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
	
	public int getCountMisApelacionesEnCurso(int id, String search) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM apelaciones AS a "
				+ "INNER JOIN partidos AS p ON a.id_disputa = p.id "
				+ "INNER JOIN solicitudes AS s ON p.solicitud = s.id "
				+ "INNER JOIN usuarios AS j_uno ON j_uno.id = s.jugador_uno "
				+ "INNER JOIN usuarios AS j_dos ON j_dos.id = s.jugador_dos "
				+ "WHERE a.estado = ? AND (j_uno.id = ? OR j_dos.id = ?) AND "
				+ "((j_uno.id != ? AND (j_uno.nombre LIKE ? OR j_uno.apodo LIKE ?)) OR "
				+ "(j_dos.id != ? AND (j_dos.nombre LIKE ? OR j_dos.apodo LIKE ?)))";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.APELACION_EN_CURSO);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, id);
		search = "%" + search + "%";
		stmt.setString(5, search);
		stmt.setString(6, search);
		stmt.setInt(7, id);
		stmt.setString(8, search);
		stmt.setString(9, search);
		
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
	
	public void asignarJueces(int id, ArrayList<negocio.Usuario> jueces) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO usuario_apelacion(id_usuario, id_disputa) "
				+ "VALUES (?,?)";
		
		for(negocio.Usuario juez : jueces) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, juez.getId());
			stmt.setInt(2, id);
			stmt.execute();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void create(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO apelaciones(id_disputa, fecha, estado) VALUES (?,?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		Calendar c = Calendar.getInstance();
		java.sql.Date date = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(2, date);
		stmt.setInt(3, negocio.Estado.APELACION_EN_CURSO);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
}
