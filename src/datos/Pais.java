package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Pais {
	private static Pais instance = null;
	
	public static Pais getInstance() {
		if(instance == null) {
			instance = new Pais();
		}
		
		return instance;
	}
	
	public ArrayList<negocio.Pais> getAll() throws SQLException, ClassNotFoundException{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from paises";
		
		stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Pais> paises = new ArrayList<negocio.Pais>();
		while(rs.next()) {
			negocio.Pais pais = new negocio.Pais();
			pais.setId(rs.getInt(1));
			pais.setNombre(rs.getString(2));
			paises.add(pais);
		}
		
		stmt.close();
		rs.close();
		conn.close();
		manager.closeConnection();
		
		return paises;
	}
}
