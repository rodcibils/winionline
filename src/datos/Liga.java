package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import negocio.Estado;

public class Liga {
	private static Liga instance = null;
	
	public static Liga getInstance() {
		if(instance == null) {
			instance = new Liga();
		}
		
		return instance;
	}
	
	public ArrayList<negocio.Liga> getLigasUsuario(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT l.id, l.nombre, l.temporada, l.fecha_inicio, l.fecha_fin "
				+ "FROM ligas AS l "
				+ "INNER JOIN usuario_liga AS ul ON ul.id_liga = l.id "
				+ "WHERE l.estado = ? AND ul.id_usuario = ? ORDER BY l.fecha_fin";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.LIGA_FINALIZADA);
		stmt.setInt(2, id);
		
		ArrayList<negocio.Liga> ligas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			
			ligas.add(liga);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return ligas;
	}
	
	public void insert(negocio.Liga liga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO ligas(nombre, temporada, fecha_inicio, fecha_fin, estado) "
				+ "VALUES (?,?,?,?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, liga.getNombre());
		stmt.setInt(2, liga.getTemporada());
		stmt.setDate(3, liga.getInicio());
		stmt.setDate(4, liga.getFin());
		stmt.setInt(5, Estado.LIGA_NO_INICIADA);
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public boolean checkIfLigaExists(negocio.Liga liga) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM ligas WHERE nombre=? and temporada=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, liga.getNombre());
		stmt.setInt(2, liga.getTemporada());
		
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			stmt.close();
			ConnectionManager.getInstance().closeConnection();
			return true;
		}
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		return false;
	}

	public ArrayList<negocio.Liga> getAll() throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas";
		
		stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public int getCountUsuariosInscriptosPorLiga(int idLiga) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		int count = 0;
		String query = "SELECT COUNT(*) from usuario_liga where id_liga=?";
		stmt = conn.prepareStatement(query);	
		stmt.setInt(1, idLiga);
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return count;	
	}
	
	
	public negocio.Liga getOne(int id) throws SQLException, ClassNotFoundException {
		negocio.Liga liga = new negocio.Liga();
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas where id=?";
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return liga;
	}
	
	public void delete(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "DELETE FROM ligas WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}

	public void update(negocio.Liga liga) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE ligas set nombre=?, temporada=?, fecha_inicio=?, fecha_fin=?, estado=? "
				+ "WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, liga.getNombre());
		stmt.setInt(2, liga.getTemporada());
		stmt.setDate(3, liga.getInicio());
		stmt.setDate(4, liga.getFin());
		stmt.setInt(5, Estado.LIGA_NO_INICIADA);
		stmt.setInt(6, liga.getId());
		
		stmt.execute();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
	}
	
	public void desinscribirse(int idUsuario, int idLiga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "DELETE FROM usuario_liga WHERE id_usuario = ? AND id_liga = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idLiga);
		
		stmt.execute();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public boolean checkLigaTerminada(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM ligas WHERE id = ? AND estado = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setInt(2, negocio.Estado.LIGA_FINALIZADA);
		
		ResultSet rs = stmt.executeQuery();
		boolean result = rs.next();
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return result;
	}

	public ArrayList<negocio.Liga> getAllPaginado(int skip, int limit) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas "
		+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, skip);
		stmt.setInt(2, limit);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public ArrayList<negocio.Liga> getAllPendientes(int id, int skip, int limit) 
			throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * FROM ligas WHERE estado = ? AND "
				+ "id NOT IN (SELECT id_liga FROM usuario_liga WHERE id_usuario = ?) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.LIGA_NO_INICIADA);
		stmt.setInt(2, id);
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			int cantidad = getCountUsuariosInscriptosPorLiga(rs.getInt(1));
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			liga.setCantidadInscriptos(cantidad);
			
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	
	public ArrayList<negocio.Liga> getAllPaginado(String toSearch, int yearSearch, int skip, int limit) throws ClassNotFoundException, SQLException {

		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		if(yearSearch != 0 && toSearch != null && !toSearch.isEmpty()) {
			String query = "SELECT * from ligas WHERE temporada = ? AND "
					+ "nombre LIKE ? LIMIT ?,?";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, yearSearch);
			toSearch = "%" + toSearch + "%";
			stmt.setString(2, toSearch);
			stmt.setInt(3, skip);
			stmt.setInt(4, limit);
		}else if(yearSearch != 0 && (toSearch == null || toSearch.isEmpty())) {
			String query = "SELECT * from ligas WHERE temporada = ? "
					+ "LIMIT ?,?";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, yearSearch);
			stmt.setInt(2, skip);
			stmt.setInt(3, limit);
		} else {
			String query = "SELECT * from ligas WHERE "
					+ "nombre LIKE ? LIMIT ?,?";
			
			stmt = conn.prepareStatement(query);
			toSearch = "%" + toSearch + "%";
			stmt.setString(1, toSearch);
			stmt.setInt(2, skip);
			stmt.setInt(3, limit);
		}
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
			
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}

	public ArrayList<negocio.Liga> getAllPendientes(int id, String search, int skip, int limit) 
			throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas WHERE estado = ? AND nombre LIKE ? AND "
				+ "id NOT IN (SELECT id_liga FROM usuario_liga WHERE id_usuario = ?) "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.LIGA_NO_INICIADA);
		search = "%" + search + "%";
		stmt.setString(2, search);
		stmt.setInt(3, id);
		stmt.setInt(4, skip);
		stmt.setInt(5, limit);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public int getCountLigas() throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM ligas";
		
		stmt = conn.prepareStatement(query);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public int getCountPendientes(int id) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM ligas WHERE estado = ? AND "
				+ "id NOT IN (SELECT id_liga FROM usuario_liga WHERE id_usuario = ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.LIGA_NO_INICIADA);
		stmt.setInt(2, id);
		ResultSet rs = stmt.executeQuery();
		
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}

	public int getCountLigas(String toSearch, int yearSearch) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		if(yearSearch != 0 && toSearch != null && !toSearch.isEmpty()) {
			String query = "SELECT COUNT(*) FROM ligas WHERE temporada = ? AND nombre LIKE ?";
			
			stmt = conn.prepareStatement(query);
			toSearch = "%" + toSearch + "%";
			stmt.setInt(1, yearSearch);
			stmt.setString(2, toSearch);
		}else if(yearSearch != 0 && (toSearch == null || toSearch.isEmpty())) {
			String query = "SELECT COUNT(*) FROM ligas WHERE temporada = ?";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, yearSearch);
		} else {
			String query = "SELECT COUNT(*) FROM ligas WHERE nombre LIKE ?";
			
			stmt = conn.prepareStatement(query);
			toSearch = "%" + toSearch + "%";
			stmt.setString(1, toSearch);
		}
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public int getCountPendientes(int id, String search) 
			throws SQLException, ClassNotFoundException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from ligas WHERE estado = ? AND nombre LIKE ? "
				+ "AND id NOT IN (SELECT id_liga FROM usuario_liga WHERE id_usuario = ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.LIGA_NO_INICIADA);
		search = "%" + search + "%";
		stmt.setString(2, search);
		stmt.setInt(3, id);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public int getCount(int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from usuario_liga where id_usuario = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public int getCountLigasActivas(int idUsuario) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_liga AS ul "
				+ "INNER JOIN ligas AS l ON l.id = ul.id_liga "
				+ "WHERE ul.id_usuario = ? AND l.estado != ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, negocio.Estado.LIGA_FINALIZADA);
		
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
	
	public ArrayList<negocio.Liga> getAll(int idUsuario, int skip, int limit) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from usuario_liga WHERE id_usuario=? "
		+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, skip);
		stmt.setInt(3, limit);		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = datos.Liga.getInstance().getOne(rs.getInt(2));			
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public int getCount(int idUsuario, String toSearch) throws SQLException, ClassNotFoundException 
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM usuario_liga AS ul "
				+ "INNER JOIN ligas AS l ON l.id = ul.id_liga "
				+ "WHERE ul.id_usuario = ? AND l.nombre LIKE ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		toSearch = "%" + toSearch + "%";
		stmt.setString(2, toSearch);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		if(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public ArrayList<negocio.Liga> getAll(int idUsuario, String toSearch, 
			int skip, int limit) throws ClassNotFoundException, SQLException 
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT ul.* FROM usuario_liga AS ul "
				+ "INNER JOIN ligas AS l ON l.id = ul.id_liga "
				+ "WHERE ul.id_usuario = ? AND l.nombre LIKE ? "
				+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		toSearch = "%" + toSearch + "%";
		stmt.setString(2, toSearch);
		stmt.setInt(3, skip);
		stmt.setInt(4, limit);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga lg = datos.Liga.getInstance().getOne(rs.getInt(2));
			ligas.add(lg);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}

	public ArrayList<negocio.Usuario> getAllUsuariosLiga(int idLiga) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from usuario_liga WHERE id_liga=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idLiga);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Usuario> usuariosLiga = new ArrayList<negocio.Usuario>();
		while(rs.next()) {
			negocio.Usuario us = datos.Usuario.getInstance().getOne(rs.getInt(1));
			usuariosLiga.add(us);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		
		return usuariosLiga;
	}
	
	public List<Integer> getTemporadas() throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT temporada FROM ligas GROUP BY temporada ORDER BY temporada DESC";
		
		stmt = conn.prepareStatement(query);
		
		List<Integer> temporadas = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			temporadas.add(rs.getInt(1));
		}
		
		return temporadas;
	}
	
	public void inscribir(int idUsuario, int idLiga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO usuario_liga(id_usuario, id_liga) VALUES (?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idLiga);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
}
