package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Resultado {
	private static Resultado instance = null;
	
	public static Resultado getInstance() {
		if(instance == null) instance = new Resultado();
		
		return instance;
	}
	
	public void insert(negocio.Resultado resultado) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO resultados(id_jugador, id_partido, goles) "
				+ "VALUES (?,?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, resultado.getJugador().getId());
		stmt.setInt(2, resultado.getPartido().getId());
		stmt.setInt(3, resultado.getGoles());
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
}
