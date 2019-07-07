package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
