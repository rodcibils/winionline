package datos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class Usuario {
	
	private static Usuario instance = null;

	private static final int ID_PARAMETRO = 1;
	
	private static final int ESTADO_ACTIVO = 1;
	private static final int ESTADO_ELIMINADO = 2;
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static Usuario getInstance()
	{
		if(instance == null) {
			instance = new Usuario();
		}
		
		return instance;
	}
	
	public boolean checkPassword(String password, negocio.Usuario usuario) throws SQLException, Exception
	{
		String key = getParametroKey();
		if (key == null) {
			key = setParametroKey();
		}
		String contrasenaEnc = EncPassword(key, password);
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();

        String query = "SELECT * FROM usuarios WHERE nombre=? and password=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, usuario.getNombre());
        stmt.setString(2, contrasenaEnc);
        
        ResultSet rs = stmt.executeQuery();
        
        boolean result = false;
        
        while(rs.next()) {
        	result = true;
        }
        
        rs.close();
        stmt.close();
        manager.closeConnection();
        
        return result;
	}
	
	public void updatePassword(negocio.Usuario usuario) throws Exception
	{
		String key = getParametroKey();
		if (key == null) {
			key = setParametroKey();
		}
		String contrasenaEnc = EncPassword(key, usuario.getPassword());
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "UPDATE usuarios SET password=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setString(1, contrasenaEnc);
		stmt.setInt(2, usuario.getId());
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	public void insert(negocio.Usuario usuario) throws Exception 
	{
		// recupero clave de encriptacion
		String key = getParametroKey();
		if (key == null) {
			key = setParametroKey();
		}
		String contrasenaEnc = EncPassword(key, usuario.getPassword());
		
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "INSERT INTO usuarios(nombre, password, fechanac, email, apodo, "
				+ "ultima_conexion, skype, ip, avatar, pais, rol, estado) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, usuario.getNombre());
		stmt.setString(2, contrasenaEnc);
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
		stmt.setInt(11, datos.Rol.JUGADOR);
		stmt.setInt(12, ESTADO_ACTIVO);
		
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	public void delete(negocio.Usuario usuario) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "UPDATE usuarios SET estado=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, ESTADO_ELIMINADO);
		stmt.setInt(2, usuario.getId());
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	public void update(negocio.Usuario usuario) throws SQLException, ClassNotFoundException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "UPDATE usuarios SET fechanac=?, apodo=?, skype=?, ip=?, avatar=?, pais=?"
				+ " WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setDate(1, usuario.getFechanac());
		stmt.setString(2, usuario.getApodo());
		stmt.setString(3, usuario.getSkype());
		stmt.setString(4, usuario.getIp());
		stmt.setString(5, usuario.getAvatar());
		stmt.setInt(6, usuario.getPais().getId());
		stmt.setInt(7, usuario.getId());
		
		stmt.execute();
		stmt.close();
		manager.closeConnection();
	}
	
	public void updateUltimaConexion(negocio.Usuario usuario) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "UPDATE usuarios SET ultima_conexion=? WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setTimestamp(1, usuario.getUltimaConexion());
		stmt.setInt(2, usuario.getId());
		
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
	
	public negocio.Usuario login(String username, String password)throws SQLException, Exception  {
		// encripto contraseña 
		String key = getParametroKey();
		String contrasenaEnc = EncPassword(key, password);
		// usuario a devolver
		negocio.Usuario usuario = new negocio.Usuario();
		int idPais;
		int idRol;
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		// busco el usuario con el email y contraseña
        String query = "SELECT * FROM usuarios WHERE nombre=? and password=? and "
        		+ "estado=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, contrasenaEnc);
        stmt.setInt(3, ESTADO_ACTIVO);
        
        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            usuario.setId(rs.getInt(1));
            usuario.setNombre(rs.getString(2));
            usuario.setFechanac(rs.getDate(4));
        	usuario.setEmail(rs.getString(5));
        	usuario.setApodo(rs.getString(6));
        	usuario.setUltimaConexion(rs.getTimestamp(7));
        	usuario.setSkype(rs.getString(8));
        	usuario.setIp(rs.getString(9));
        	usuario.setAvatar(rs.getString(10));
        	
        	idPais = rs.getInt(11);
        	idRol = rs.getInt(12);
        } else {
            return null;
        }
        stmt.close();
		manager.closeConnection();
		usuario.setRol(Rol.getInstance().getOne(idRol));
    	usuario.setPais(Pais.getInstance().getOne(idPais));
        
    	return usuario;
    }
	
	public String getParametroKey()throws SQLException, Exception {
		String key = new String();
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		// busco la key para encriptar
        String query = "SELECT * FROM parametros WHERE parametroid=?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_PARAMETRO);
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
        	key = rs.getString(3);
        }
        
        rs.close();
        stmt.close();
		manager.closeConnection();
		return key;
	}
	
	public String setParametroKey()throws SQLException, Exception {
		// genero la nueva key random
		Random rand = new Random();
		int keyInt = rand.nextInt();
		if (keyInt < 0) {
			keyInt = keyInt * -1;
		}
		String key = Integer.toString(keyInt);
		
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		// actualizo la key con el nuevo valor
        String query = "update parametros set parametros.key = ? where parametroid = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, key);
        stmt.setInt(2, ID_PARAMETRO);
        
        stmt.execute();
		stmt.close();
		manager.closeConnection();
        
		return key;
	}
	
	public String getParametroPath()throws SQLException, Exception {
		String path = new String();
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		// busco el path de los avatars
        String query = "SELECT * FROM parametros WHERE parametroid=?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_PARAMETRO);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
        	path = rs.getString(2);
        }
        rs.close();
        stmt.close();
		manager.closeConnection();
		return path;
	}

	public static String bytesToHex(byte[] bytes) {
	  char[] hexChars = new char[bytes.length * 2];
	  int v;
	  for (int j = 0; j < bytes.length; j++) {
	    v = bytes[j] & 0xFF;
	    hexChars[j * 2] = hexArray[v >>> 4];
	    hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	  }
	  return new String(hexChars);
	}
	
	// encripto contraseña
	public static String EncPassword(String key, String in) {
	  try {
	    MessageDigest md = MessageDigest
	        .getInstance("SHA-256");
	    // antepongo key
	    md.update(key.getBytes());
	    md.update(in.getBytes());

	    byte[] out = md.digest();
	    return bytesToHex(out);
	  } catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	  }
	  return "";
	}
}
