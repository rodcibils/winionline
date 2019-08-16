package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import negocio.Estado;
import utils.Utils;

public class Usuario {
	private static Usuario instance = null;
	
	public static Usuario getInstance()
	{
		if(instance == null) {
			instance = new Usuario();
		}
		
		return instance;
	}
	
	public ArrayList<negocio.Usuario> getJuecesApelacion(int id) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT id FROM usuarios WHERE rol = ? "
				+ "AND id NOT IN (SELECT s.jugador_uno FROM disputas AS d "
				+ "INNER JOIN partidos AS p ON d.id_partido = p.id "
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud "
				+ "WHERE d.id_partido = ?) AND id NOT IN (SELECT s.jugador_dos "
				+ "FROM disputas AS d INNER JOIN partidos AS p ON d.id_partido = p.id " 
				+ "INNER JOIN solicitudes AS s ON s.id = p.solicitud " 
				+ "WHERE d.id_partido = ?) LIMIT ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, datos.Rol.ADMINISTRADOR);
		stmt.setInt(2, id);
		stmt.setInt(3, id);
		stmt.setInt(4, negocio.Apelacion.CANT_JUECES);
		
		ArrayList<negocio.Usuario> jueces = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			negocio.Usuario juez = new negocio.Usuario();
			juez.setId(rs.getInt(1));
			jueces.add(juez);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return jueces;
	}
	
	public negocio.Usuario getOne(int id) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM usuarios WHERE id=?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		negocio.Usuario usuario = null;
		if(rs.next()) {
			usuario = new negocio.Usuario();
			usuario.setId(rs.getInt(1));
			usuario.setNombre(rs.getString(2));
			usuario.setPassword(rs.getString(3));
			usuario.setFechanac(rs.getDate(4));
			usuario.setEmail(rs.getString(5));
			usuario.setApodo(rs.getString(6));
			usuario.setUltimaConexion(rs.getTimestamp(7));
			usuario.setSkype(rs.getString(8));
			usuario.setIp(rs.getString(9));
			usuario.setAvatar(rs.getString(10));
			usuario.setPais(datos.Pais.getInstance().getOne(rs.getInt(11)));
			usuario.setRol(datos.Rol.getInstance().getOne(rs.getInt(12)));
			usuario.setEstado(datos.Estado.getInstance().getOne(rs.getInt(13)));
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return usuario;
	}
	
	public ArrayList<negocio.Usuario> getAll(negocio.Usuario usuarioActual) throws SQLException, ClassNotFoundException{
		PreparedStatement stmt;
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection conn = manager.getConnection();
		
		String query = "SELECT * from usuarios WHERE estado= ? AND id != ?";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.USUARIO_ACTIVO);
		stmt.setInt(2, usuarioActual.getId());
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<negocio.Usuario> usuarios = new ArrayList<negocio.Usuario>();
		while(rs.next()) {
			negocio.Usuario usuario = new negocio.Usuario();
			negocio.Pais pais = datos.Pais.getInstance().getOne(rs.getInt(11));
			negocio.Rol rol = datos.Rol.getInstance().getOne(rs.getInt(12));
			negocio.Estado estado = datos.Estado.getInstance().getOne(rs.getInt(13)); 
			usuario.setId(rs.getInt(1));
			usuario.setNombre(rs.getString(2));
			usuario.setPassword(rs.getString(3));
			usuario.setFechanac(rs.getDate(4));
			usuario.setEmail(rs.getString(5));
			usuario.setApodo(rs.getString(6));
			usuario.setUltimaConexion(rs.getTimestamp(7));
			usuario.setSkype(rs.getString(8));
			usuario.setIp(rs.getString(9));
			usuario.setAvatar(rs.getString(10));			
			usuario.setPais(pais);
			usuario.setRol(rol);
			usuario.setEstado(estado);
			
			usuarios.add(usuario);
		}
		
		stmt.close();
		rs.close();
		manager.closeConnection();
		
		return usuarios;
	}
	
	public int getAllCount(negocio.Usuario usuarioActual) 
			throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT COUNT(*) from usuarios WHERE estado = ? AND id != ?";
		
		stmt = conn.prepareStatement(query);		
		stmt.setInt(1, negocio.Estado.USUARIO_ACTIVO);
		stmt.setInt(2, usuarioActual.getId());		
		
		ResultSet rs = stmt.executeQuery();
		int rowsCount = 0;
		while(rs.next()) {
			rowsCount = rs.getInt(1);
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return rowsCount;
	}
	
	public ArrayList<negocio.Usuario> getUsuariosPagination
	(int skip, int limit, negocio.Usuario usuarioActual) throws ClassNotFoundException, SQLException
{
	PreparedStatement stmt;
	Connection conn = ConnectionManager.getInstance().getConnection();
	
	String query = "SELECT * from usuarios WHERE estado = ? AND id != ? LIMIT ?,?";
	
	stmt = conn.prepareStatement(query);
	stmt.setInt(1, negocio.Estado.USUARIO_ACTIVO);
	stmt.setInt(2, usuarioActual.getId());	
	stmt.setInt(3, skip);
	stmt.setInt(4, limit);
	
	ResultSet rs = stmt.executeQuery();
	ArrayList<negocio.Usuario> usuarios = new ArrayList<>();
	while(rs.next()) {
		negocio.Usuario usuario = new negocio.Usuario();
		negocio.Pais pais = datos.Pais.getInstance().getOne(rs.getInt(11));
		negocio.Rol rol = datos.Rol.getInstance().getOne(rs.getInt(12));
		negocio.Estado estado = datos.Estado.getInstance().getOne(rs.getInt(13)); 
		usuario.setId(rs.getInt(1));
		usuario.setNombre(rs.getString(2));
		usuario.setPassword(rs.getString(3));
		usuario.setFechanac(rs.getDate(4));
		usuario.setEmail(rs.getString(5));
		usuario.setApodo(rs.getString(6));
		usuario.setUltimaConexion(rs.getTimestamp(7));
		usuario.setSkype(rs.getString(8));
		usuario.setIp(rs.getString(9));
		usuario.setAvatar(rs.getString(10));			
		usuario.setPais(pais);
		usuario.setRol(rol);
		usuario.setEstado(estado);
		
		usuarios.add(usuario);
	}
	
	rs.close();
	stmt.close();
	ConnectionManager.getInstance().closeConnection();
	
	return usuarios;
}
	
	public int getCountUsuariosFiltered
	(String toSearch, negocio.Usuario usuarioActual) throws ClassNotFoundException, SQLException
{
	PreparedStatement stmt;
	Connection conn = ConnectionManager.getInstance().getConnection();
	
	String query = "SELECT * from usuarios WHERE estado = ? AND id != ?";
	
	stmt = conn.prepareStatement(query);
	stmt.setInt(1, negocio.Estado.USUARIO_ACTIVO);
	stmt.setInt(2, usuarioActual.getId());
	
	
	ResultSet rs = stmt.executeQuery();
	int count = 0;
	while(rs.next()) {
		negocio.Usuario usuario = datos.Usuario.getInstance().getOne(rs.getInt(1));
		if(usuario.getNombre().contains(toSearch) || usuario.getApodo().contains(toSearch))
		{
			++count;
		}
	}
	
	rs.close();
	stmt.close();
	ConnectionManager.getInstance().closeConnection();
	
	return count;
}
	
	public ArrayList<negocio.Usuario> getUsuariosPagination
	(int skip, int limit, String toSearch, negocio.Usuario usuarioActual) 
	throws ClassNotFoundException, SQLException
{
	PreparedStatement stmt;
	Connection conn = ConnectionManager.getInstance().getConnection();
	
	String query = "SELECT * from usuarios WHERE estado = ? AND id != ?";
	
	stmt = conn.prepareStatement(query);
	stmt.setInt(1, negocio.Estado.USUARIO_ACTIVO);
	stmt.setInt(2, usuarioActual.getId());
	
	ResultSet rs = stmt.executeQuery();
	ArrayList<negocio.Usuario> usuarios = new ArrayList<>();
	while(rs.next()) {
		negocio.Usuario usu = datos.Usuario.getInstance().getOne(rs.getInt(1));
		if(usu.getNombre().contains(toSearch) || usu.getApodo().contains(toSearch))
		{
			negocio.Usuario usuario = new negocio.Usuario();
			negocio.Pais pais = datos.Pais.getInstance().getOne(rs.getInt(11));
			negocio.Rol rol = datos.Rol.getInstance().getOne(rs.getInt(12));
			negocio.Estado estado = datos.Estado.getInstance().getOne(rs.getInt(13)); 
			usuario.setId(rs.getInt(1));
			usuario.setNombre(rs.getString(2));
			usuario.setPassword(rs.getString(3));
			usuario.setFechanac(rs.getDate(4));
			usuario.setEmail(rs.getString(5));
			usuario.setApodo(rs.getString(6));
			usuario.setUltimaConexion(rs.getTimestamp(7));
			usuario.setSkype(rs.getString(8));
			usuario.setIp(rs.getString(9));
			usuario.setAvatar(rs.getString(10));			
			usuario.setPais(pais);
			usuario.setRol(rol);
			usuario.setEstado(estado);
			
			usuarios.add(usuario);
		}
	}
	
	ArrayList<negocio.Usuario> filteredUsuarios = new ArrayList<>();
	
	if(skip+limit < usuarios.size()) { 
		for(int i=skip; i<skip+limit; ++i)
		{
			filteredUsuarios.add(usuarios.get(i));
		}
	} else {
		for(int i=skip; i<usuarios.size(); ++i) {
			filteredUsuarios.add(usuarios.get(i));
		}
	}
	
	rs.close();
	stmt.close();
	ConnectionManager.getInstance().closeConnection();
	
	return filteredUsuarios;
}
	
	
	public boolean checkPassword(String password, negocio.Usuario usuario) throws SQLException, Exception
	{
		String key = datos.Parametro.getInstance().getKey();
		if (key.isEmpty()) {
			key = datos.Parametro.getInstance().generateKey();
		}
		String contrasenaEnc = Utils.encrypt(key, password);
		
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
		String key = datos.Parametro.getInstance().getKey();
		if (key.isEmpty()) {
			key = datos.Parametro.getInstance().generateKey();
		}
		String contrasenaEnc = Utils.encrypt(key, usuario.getPassword());
		
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
		String key = datos.Parametro.getInstance().getKey();
		if (key.isEmpty()) {
			key = datos.Parametro.getInstance().generateKey();
		}
		String contrasenaEnc = Utils.encrypt(key, usuario.getPassword());
		
		
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
		stmt.setInt(12, Estado.USUARIO_ACTIVO);
		
		
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
		stmt.setInt(1, Estado.USUARIO_ELIMINADO);
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
		String key = datos.Parametro.getInstance().getKey();
		String contrasenaEnc = Utils.encrypt(key, password);
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
        stmt.setInt(3, Estado.USUARIO_ACTIVO);
        
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
        	stmt.close();
    		manager.closeConnection();
            return null;
        }
        rs.close();
        stmt.close();
		manager.closeConnection();
		usuario.setRol(Rol.getInstance().getOne(idRol));
    	usuario.setPais(Pais.getInstance().getOne(idPais));
        
    	return usuario;
    }

	
}
