package negocio;

import java.sql.Date;
import java.sql.Timestamp;

public class Usuario {
	private int id;
	private String nombre;
	private String password;
	private String email;
	private Date fechanac;
	private String apodo;
	private Timestamp ultima_conexion;
	private Pais pais;
	private String skype;
	private String ip;
	private String avatar;
	private Rol rol;
	private Estado estado;
	
	public boolean isAdmin() {
		return rol.getId() == datos.Rol.ADMINISTRADOR;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getFechanac() {
		return fechanac;
	}
	
	public void setFechanac(java.util.Date fechanac) {
		Date sqlDate = new Date(fechanac.getTime());
		this.fechanac = sqlDate;
	}
	
	public void setFechanac(Date fechanac) {
		this.fechanac = fechanac;
	}
	
	public String getApodo() {
		return apodo;
	}
	
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}
	
	public Timestamp getUltimaConexion() {
		return ultima_conexion;
	}
	
	public void setUltimaConexion(Timestamp ultima_conexion) {
		this.ultima_conexion = ultima_conexion;
	}
	
	public Pais getPais() {
		return pais;
	}
	
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	
	public String getSkype() {
		return skype;
	}
	
	public void setSkype(String skype) {
		this.skype = skype;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
}
