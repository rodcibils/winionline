package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Estado {
	private static Estado instance = null;
	
	public static Estado getInstance() {
		if(instance == null)
			instance = new Estado();
		
		return instance;
	}
	
	public negocio.Estado getOne(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM estados WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Estado estado = null;
		if(rs.next()) {
			estado = new negocio.Estado();
			estado.setId(rs.getInt(1));
			estado.setDescripcion(rs.getString(2));
		}
		
		stmt.close();
		rs.close();
		ConnectionManager.getInstance().closeConnection();
		
		return estado;
	}
}
