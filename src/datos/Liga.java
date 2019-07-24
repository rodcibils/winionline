package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
			ligas.add(liga);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return ligas;
	}
	
	public ArrayList<negocio.Liga> getAllPaginado(String toSearch, int skip, int limit) throws ClassNotFoundException, SQLException {
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

	public int getCountLigasFiltered(String toSearch) throws SQLException, ClassNotFoundException {
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
				++rowsCount;
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
}
