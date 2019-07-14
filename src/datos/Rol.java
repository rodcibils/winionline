package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Rol {
	
	public static int ADMINISTRADOR = 1;
	public static int JUGADOR = 2;
	
	private static Rol instance = null;
	
	public static Rol getInstance() {
		if(instance == null) {
			instance = new Rol();
		}
		
		return instance;
	}
	
	public ArrayList<negocio.Rol> getAll() throws SQLException, ClassNotFoundException{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from roles";
		
		stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Rol> roles = new ArrayList<negocio.Rol>();
		while(rs.next()) {
			negocio.Rol rol = new negocio.Rol();
			rol.setId(rs.getInt(1));
			rol.setNombre(rs.getString(2));
			rol.setDescripcion(rs.getString(3));
			roles.add(rol);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return roles;
	}
	
	public negocio.Rol getOne(int id) throws ClassNotFoundException, SQLException{
		negocio.Rol rol = new negocio.Rol();
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from roles where id=?";
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			rol.setId(rs.getInt(1));
			rol.setNombre(rs.getString(2));
			rol.setDescripcion(rs.getString(3));
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return rol;
	}
}
