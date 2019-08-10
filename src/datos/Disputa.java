package datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

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
		
		String query = "INSERT INTO disputas(id_partido, fecha, vencimiento, "
				+ "estado) VALUES (?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		Calendar c = Calendar.getInstance();
		java.sql.Date sqlToday = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(2, sqlToday);
		c.add(Calendar.DATE, negocio.Disputa.MAX_DIAS_DISPUTA);
		java.sql.Date sqlVencimiento = new java.sql.Date(c.getTime().getTime());
		stmt.setDate(3, sqlVencimiento);
		stmt.setInt(4, negocio.Estado.DISPUTA_EN_CURSO);
		
		stmt.execute();
		
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public int getCountDisputasEnCurso(int idUsuario) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON p.id = d.id_partido "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "WHERE s.jugador_uno = ? OR s.jugador_dos = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idUsuario);
		stmt.setInt(2, idUsuario);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return count;
	}
}
