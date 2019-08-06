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
	
	public ArrayList<negocio.Liga> getAllByEstado(int idEstado) throws SQLException, ClassNotFoundException {
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas where estado=?";
		stmt = conn.prepareStatement(query);	
		stmt.setInt(1, idEstado);
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			int cantidad = getCountUsuariosInscriptosPorLiga(rs.getInt(1));
			negocio.Liga liga = new negocio.Liga();
			liga.setId(rs.getInt(1));
			liga.setNombre(rs.getString(2));
			liga.setTemporada(rs.getInt(3));
			liga.setInicio(rs.getDate(4));
			liga.setFin(rs.getDate(5));
			liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
			liga.setCantidadInscriptos(cantidad);
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
	
	public void delete(negocio.Liga liga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "DELETE FROM ligas WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, liga.getId());
		
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
	
	public ArrayList<negocio.Liga> getAllPaginadoByEstado(int skip, int limit, int idEstado) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas WHERE estado=? "
		+ "LIMIT ?,?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idEstado);
		stmt.setInt(2, skip);
		stmt.setInt(3, limit);		
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
		
		String query = "SELECT * from ligas ";
		
		stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga lg = datos.Liga.getInstance().getOne(rs.getInt(1));
			if(lg.getNombre().contains(toSearch))
			{
				if (yearSearch == 0)
				{
					negocio.Liga liga = new negocio.Liga();
					liga.setId(rs.getInt(1));
					liga.setNombre(rs.getString(2));
					liga.setTemporada(rs.getInt(3));
					liga.setInicio(rs.getDate(4));
					liga.setFin(rs.getDate(5));
					liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
					ligas.add(liga);
				}else
				{
					if (yearSearch == lg.getTemporada())
					{
						negocio.Liga liga = new negocio.Liga();
						liga.setId(rs.getInt(1));
						liga.setNombre(rs.getString(2));
						liga.setTemporada(rs.getInt(3));
						liga.setInicio(rs.getDate(4));
						liga.setFin(rs.getDate(5));
						liga.setEstado(datos.Estado.getInstance().getOne(rs.getInt(6)));
						ligas.add(liga);
					}
				}
			}
		}
		
		ArrayList<negocio.Liga> filteredLigas = new ArrayList<>();
		
		if(skip+limit < ligas.size()) { 
			for(int i=skip; i<skip+limit; ++i)
			{
				filteredLigas.add(ligas.get(i));
			}
		} else {
			for(int i=skip; i<ligas.size(); ++i) {
				filteredLigas.add(ligas.get(i));
			}
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}

	public ArrayList<negocio.Liga> getLigasPaginationByEstado(String toSearch, int skip, int limit, int idEstado) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from ligas WHERE estado=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idEstado);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga lg = datos.Liga.getInstance().getOne(rs.getInt(1));
			if(lg.getNombre().contains(toSearch))
			{
				negocio.Liga liga = new negocio.Liga();
				liga.setId(rs.getInt(1));
				liga.setNombre(rs.getString(2));
				liga.setTemporada(rs.getInt(3));
				liga.setInicio(rs.getDate(4));
				liga.setFin(rs.getDate(5));
				ligas.add(liga);
			}
		}
		
		ArrayList<negocio.Liga> filteredLigas = new ArrayList<>();
		
		if(skip+limit < ligas.size()) { 
			for(int i=skip; i<skip+limit; ++i)
			{
				filteredLigas.add(ligas.get(i));
			}
		} else {
			for(int i=skip; i<ligas.size(); ++i) {
				filteredLigas.add(ligas.get(i));
			}
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public int getCountLigas() throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from ligas";
		
		stmt = conn.prepareStatement(query);
		
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
	
	public int getCountLigasByEstado(int idEstado) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from ligas where estado=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idEstado);
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

	public int getCountLigasFiltered(String toSearch, int yearSearch) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from ligas";
		
		stmt = conn.prepareStatement(query);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			negocio.Liga liga = datos.Liga.getInstance().getOne(rs.getInt(1));
			if(liga.getNombre().contains(toSearch))
			{
				if (yearSearch == 0)
				{
					++rowsCount;
				}else
				{
					if (yearSearch == liga.getTemporada())
					{
						++rowsCount;
					}
				}
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public int getCountLigasFilteredByEstado(String toSearch, int idEstado) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from ligas WHERE estado=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idEstado);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			negocio.Liga liga = datos.Liga.getInstance().getOne(rs.getInt(1));
			if(liga.getNombre().contains(toSearch))
			{
				++rowsCount;
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public ArrayList<negocio.Liga> getAllByUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from usuario_liga WHERE id_usuario = ?";
		
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		ResultSet rs = stmt.executeQuery();
		
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga liga = datos.Liga.getInstance().getOne(rs.getInt(2));			
//			liga.setId(rs.getInt(1));
//			liga.setNombre(rs.getString(2));
//			liga.setTemporada(rs.getInt(3));
//			liga.setInicio(rs.getDate(4));
//			liga.setFin(rs.getDate(5));
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public int getCountLigasByUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from usuario_liga where id_usuario=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
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
	
	public ArrayList<negocio.Liga> getAllPaginadoByUsuario(int skip, int limit, int idUsuario) throws ClassNotFoundException, SQLException {
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
	
	public int getCountLigasFilteredByUsuario(String toSearch, int idUsuario) throws SQLException, ClassNotFoundException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from usuario_liga WHERE id_usuario=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			negocio.Liga liga = datos.Liga.getInstance().getOne(rs.getInt(2));
			if(liga.getNombre().contains(toSearch))
			{
				++rowsCount;
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public ArrayList<negocio.Liga> getLigasPaginationByUsuario(String toSearch, int skip, int limit, int idUsuario) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from usuario_liga WHERE id_usuario=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Liga> ligas = new ArrayList<negocio.Liga>();
		while(rs.next()) {
			negocio.Liga lg = datos.Liga.getInstance().getOne(rs.getInt(2));
			if(lg.getNombre().contains(toSearch))
			{				
				ligas.add(lg);
			}
		}
		
		ArrayList<negocio.Liga> filteredLigas = new ArrayList<>();
		
		if(skip+limit < ligas.size()) { 
			for(int i=skip; i<skip+limit; ++i)
			{
				filteredLigas.add(ligas.get(i));
			}
		} else {
			for(int i=skip; i<ligas.size(); ++i) {
				filteredLigas.add(ligas.get(i));
			}
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
	
	public boolean insertUsuarioLiga(int idUsuario, int idLiga) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		PreparedStatement stmtCount;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String queryCount = "SELECT * FROM usuario_liga WHERE id_usuario = ? AND id_liga= ?";
		stmtCount = conn.prepareStatement(queryCount);
		stmtCount.setInt(1, idUsuario);
		stmtCount.setInt(2, idLiga);
		ResultSet rs = stmtCount.executeQuery();		
		int rowsCount = 0;
		while(rs.next()) {
			rowsCount++;
		}
		if(rowsCount == 0)
		{		
			String query = "INSERT INTO usuario_liga(id_usuario, id_liga) "
					+ "VALUES (?,?)";
			
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idLiga);
			
			stmt.execute();
			stmt.close();
			ConnectionManager.getInstance().closeConnection();
			return true;
		}
		else
		{
			stmtCount.close();
			ConnectionManager.getInstance().closeConnection();
			return false;
		}
			
		
	}
}
