package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class Apelacion {
	private static Apelacion instance = null;
	
	public static Apelacion getInstance() {
		if(instance == null) instance = new Apelacion();
		
		return instance;
	}
	
	public void asignarJueces(int id, ArrayList<negocio.Usuario> jueces) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO usuario_apelacion(id_usuario, id_disputa) "
				+ "VALUES (?,?)";
		
		for(negocio.Usuario juez : jueces) {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, juez.getId());
			stmt.setInt(2, id);
			stmt.execute();
			stmt.close();
		}
		
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void create(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "INSERT INTO apelaciones(id_disputa, fecha, estado) VALUES (?,?,?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		Calendar c = Calendar.getInstance();
		java.sql.Date date = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(2, date);
		stmt.setInt(3, negocio.Estado.APELACION_EN_CURSO);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
}
