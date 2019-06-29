package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
				"ultima_conexion, skype, ip, avatar, pais) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, usuario.getNombre());
		stmt.setString(2, usuario.getPassword());
		stmt.setDate(3, usuario.getFechanac());
		stmt.setString(4, usuario.getEmail());
		stmt.setString(5, usuario.getApodo());
		// la fecha de ultima conexion al registrar usuario es la fecha del registro mismo
		LocalDateTime todayDate = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(todayDate);
		stmt.setTimestamp(6, timestamp);;
		stmt.setString(7, usuario.getSkype());
		stmt.setString(8, usuario.getIp());
		stmt.setString(9, usuario.getAvatar());
		stmt.setInt(10, usuario.getPais().getId());
		
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
