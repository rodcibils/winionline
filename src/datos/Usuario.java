package datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Usuario {
	
	private static Usuario instance = null;
	private static final int ROL_JUGADOR = 2;
	
	public static Usuario getInstance()
	{
		if(instance == null) {
			instance = new Usuario();
		}
		
		return instance;
	}
	
	public void insert(negocio.Usuario usuario) throws SQLException, ClassNotFoundException 
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "INSERT INTO usuarios(nombre, password, fechanac, email, apodo, " +
				"ultima_conexion, pais, skype, ip, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, usuario.getNombre());
		stmt.setString(2, usuario.getPassword());
		stmt.setDate(3, usuario.getFechanac());
		stmt.setString(4, usuario.getEmail());
		stmt.setString(5, usuario.getApodo());
		// la fecha de ultima conexion al registrar usuario es la fecha del registro mismo
		Date todayDate = new Date(System.currentTimeMillis());
		stmt.setDate(6, todayDate);
		stmt.setString(7, usuario.getPais());
		stmt.setString(8, usuario.getSkype());
		stmt.setString(9, usuario.getIp());
		stmt.setString(10, usuario.getAvatar());
		
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		if(rs.next()) {
			int usuarioId = rs.getInt(1);
			setNuevoUsuarioRol(usuarioId);
		}
		rs.close();
		stmt.close();
		manager.closeConnection();
	}
	
	public void setNuevoUsuarioRol(int id) throws SQLException, ClassNotFoundException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "INSERT INTO usuario_rol(id_usuario, id_rol) VALUES (?, ?)";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		// todos los usuarios nuevos tienen el rol de jugador por defecto
		stmt.setInt(2, ROL_JUGADOR);
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
}
