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
		
		String query = "INSERT INTO usuarios(nombre, password, fechanac, email, apodo, "
				+ "ultima_conexion, skype, ip, avatar, pais, rol) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
		// seteo como predeterminado el rol como jugador
		stmt.setInt(11, ROL_JUGADOR);	
		
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	
	public void updateUsuarioRol(int rol, int id) throws SQLException, ClassNotFoundException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "update usuarios set rol = ? where id = ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, rol);
		stmt.setInt(2, id);
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	public boolean checkIfUserExists(String nombre) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT id FROM usuarios WHERE nombre=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, nombre);
		
		ResultSet rs = stmt.executeQuery();
		boolean result = false;
		if(rs.next()) {
			result = true;
		}
		
		stmt.close();
		manager.closeConnection();
		return result;
	}
	public negocio.Usuario autenticate(String usermail, String password)throws SQLException, Exception  {
		// usuario a devolver
		negocio.Usuario usuario = new negocio.Usuario();
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		// busco el usuario con el email y contraseña
        String query = "SELECT * FROM usuarios WHERE email=? and password=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, usermail);
        stmt.setString(2, password);
        
        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            usuario.setId(rs.getInt(1));
            usuario.setNombre(rs.getString(2));
            usuario.setEmail(rs.getString(4));
        } else {
            return null;
        }
        stmt.close();
		manager.closeConnection();
        return usuario;
    }
}
