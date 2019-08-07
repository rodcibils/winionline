package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Disputa 
{
	private static Disputa instance = null;
	
	public static Disputa getInstance() {
		if(instance == null) {
			instance = new Disputa();
		}
		
		return instance;
	}
	
	public void insert(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO disputas(id_partido) VALUES (?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
}
