package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solicitud {
	private static Solicitud instance = null;
	
	public static Solicitud getInstance() {
		if(instance == null)
			instance = new Solicitud();
		
		return instance;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesEnviadasAmistososPendientes
		(negocio.Usuario jugadorUno) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * from solicitudes WHERE estado=? AND jugador_uno=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorUno.getId());
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			negocio.Usuario jugadorDos = datos.Usuario.getInstance().getOne(rs.getInt(5));
			if(jugadorDos.getEstado().getId() != negocio.Estado.USUARIO_ELIMINADO) {
				negocio.Solicitud solicitud = new negocio.Solicitud();
				solicitud.setId(rs.getInt(1));
				solicitud.setFecha(rs.getDate(2));
				solicitud.setEstado(datos.Estado.getInstance().getOne(rs.getInt(3)));
				solicitud.setJugadorUno(jugadorUno);
				solicitud.setJugadorDos(jugadorDos);
				solicitudes.add(solicitud);
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
	
	public ArrayList<negocio.Solicitud> getSolicitudesRecibidasAmistososPendientes
		(negocio.Usuario jugadorDos) throws ClassNotFoundException, SQLException
	{
		PreparedStatement stmt;
		Connection conn = ConnectionManager.getInstance().getConnection();
		
		String query = "SELECT * FROM solicitudes WHERE estado=? AND jugador_dos=? "
				+ "AND liga IS NULL";
		
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, negocio.Estado.SOLICITUD_PENDIENTE);
		stmt.setInt(2, jugadorDos.getId());
		
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<negocio.Solicitud> solicitudes = new ArrayList<>();
		while(rs.next()) {
			int idEstado = rs.getInt(3);
			negocio.Usuario jugadorUno = datos.Usuario.getInstance().getOne(rs.getInt(4));
			if(idEstado == negocio.Estado.SOLICITUD_PENDIENTE
				&& jugadorUno.getEstado().getId() != negocio.Estado.USUARIO_ELIMINADO) {
				negocio.Solicitud solicitud = new negocio.Solicitud();
				solicitud.setId(rs.getInt(1));
				solicitud.setFecha(rs.getDate(2));
				solicitud.setEstado(datos.Estado.getInstance().getOne(idEstado));
				solicitud.setJugadorUno(jugadorUno);
				solicitud.setJugadorDos(jugadorDos);
				solicitudes.add(solicitud);
			}
		}
		
		rs.close();
		stmt.close();
		ConnectionManager.getInstance().closeConnection();
		
		return solicitudes;
	}
}

