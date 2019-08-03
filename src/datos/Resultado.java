package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

	public ArrayList<negocio.Resultado> getResultadoPartido(int idPartido) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM resultados WHERE id_partido=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, idPartido);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Resultado> resultado = new ArrayList<negocio.Resultado>();
		while(rs.next()) {
			negocio.Resultado res = new negocio.Resultado();
			res.setJugador(datos.Usuario.getInstance().getOne(rs.getInt(1)));
			res.setPartido(datos.Partido.getInstance().getOne(rs.getInt(2)));
			res.setGoles(rs.getInt(3));
			resultado.add(res);
		}
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return resultado;
	}
		
	
	public negocio.Resultado getOne(negocio.Usuario jugador, negocio.Partido partido) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM resultados WHERE id_jugador=? AND id_partido=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, jugador.getId());
		stmt.setInt(2, partido.getId());
		
		ResultSet rs = stmt.executeQuery();
		negocio.Resultado resultado = null;
		if(rs.next()) {
			resultado = new negocio.Resultado();
			resultado.setJugador(jugador);
			resultado.setPartido(partido);
			resultado.setGoles(rs.getInt(3));
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return resultado;
	}
}
