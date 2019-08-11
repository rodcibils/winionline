package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Parametro {
	private static Parametro instance = null;
	
	private static final int KEY = 1;
	private static final int AVATARS = 2;
	private static final int EVIDENCIAS = 3;
	
	public static Parametro getInstance() {
		if(instance == null) instance = new Parametro();
		
		return instance;
	}
	
	public String getKey() throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT parametro FROM parametros WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, KEY);
		
		ResultSet rs = stmt.executeQuery();
		String key = "";
		if(rs.next()) {
			key = rs.getString(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return key;
	}
	
	public String generateKey() throws ClassNotFoundException, SQLException
	{
		Random rand = new Random();
		int key = rand.nextInt();
		
		if(key < 0) key *= -1;
		
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "UPDATE parametros SET parametro = ? WHERE id = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, Integer.toString(key));
		stmt.setInt(2, KEY);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return Integer.toString(key);
	}
	
	public String getAvatarsPath() throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT parametro FROM parametros WHERE id = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, AVATARS);
		
		ResultSet rs = stmt.executeQuery();
		String path = "";
		if(rs.next()) {
			path = rs.getString(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return path;
	}
	
	public String getEvidenciasPath() throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT parametro FROM parametros WHERE id = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, EVIDENCIAS);
		
		ResultSet rs = stmt.executeQuery();
		String path = "";
		if(rs.next()) {
			path = rs.getString(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return path;
	}
}
