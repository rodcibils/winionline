package negocio;

import java.util.Date;

public class Usuario {
	private int id;
	private String usuario;
	private String password;
	private String email;
	private Date fechanac;
	private String apodo;
	private Date ultima_conexion;
	private String pais;
	private String skype;
	private String ip;
	private String avatar;
	private int id_rol;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	
	public void setFechanac(Date fechanac) {
		this.fechanac = fechanac;
	}
	
	public String getApodo() {
		return apodo;
	}
	
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}
	
	public Date getUltima_conexion() {
		return ultima_conexion;
	}
	
	public void setUltima_conexion(Date ultima_conexion) {
		this.ultima_conexion = ultima_conexion;
	}
	
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
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

	public int getIdRol() {
		return id_rol;
	}

	public void setIdRol(int id_rol) {
		this.id_rol = id_rol;
	}
	
}
