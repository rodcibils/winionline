package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.UsuarioEstadisticas;
import utils.Log;
import utils.Reportes;

/**
 * Servlet implementation class StatsLigaServlet
 */
@WebServlet("/estadisticasLiga")
public class StatsLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatsLigaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try 
		{
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			
			int idLiga = Integer.parseInt(request.getParameter("id"));
			request.setAttribute("id", idLiga);
			
			String desafiar = request.getParameter("desafiar");
			if(desafiar != null && !desafiar.isEmpty()) {
				datos.Solicitud.getInstance().createSolicitudLiga(usuario.getId(), 
						Integer.parseInt(desafiar), idLiga);
				request.setAttribute("challenge_success", "La solicitud de partido al rival fue "
						+ "enviada correctamente");
			}
			
			ArrayList<negocio.Usuario> usuariosLiga = datos.Liga.getInstance()
					.getAllUsuariosLiga(idLiga);
			ArrayList<negocio.UsuarioEstadisticas> estadisticasUsuarios = 
					new ArrayList<negocio.UsuarioEstadisticas>();
			
			// Busco los stats de los usuarios de la liga
			for (negocio.Usuario user : usuariosLiga) {
				negocio.UsuarioEstadisticas ue = new UsuarioEstadisticas();
				
				int golesContra = 0;
				int golesFavor = 0;
				int partGanados = 0;
				int partPerdidos = 0;
				int partEmpatados = 0;
				ArrayList<negocio.Solicitud> solis = datos.Solicitud
						.getInstance().getAllSolicitudesLigaUsuario(idLiga, user.getId());
				
				for (negocio.Solicitud sol : solis) {
					negocio.Partido partido = datos.Partido.getInstance()
							.getOnePartidoSolicitud(sol);
					ArrayList<negocio.Resultado> resultado = datos.Resultado
							.getInstance().getResultadoPartido(partido.getId());
					int golesJugadorUsuario = 0;
					int golesJugadorRival = 0;
					for (negocio.Resultado re : resultado) {
						if (re.getJugador().getId() == user.getId())
						{
							golesJugadorUsuario = re.getGoles();
						}else
						{
							golesJugadorRival = re.getGoles();
						}
					}
					if (partido != null && resultado != null)
					{
						golesFavor += golesJugadorUsuario;
						golesContra += golesJugadorRival;
						if (golesJugadorUsuario > golesJugadorRival) {
							++partGanados;
						} else {
							if (golesJugadorRival > golesJugadorUsuario) {
								++partPerdidos;
							} else {
								++partEmpatados;
							}
						}
					}
				}
				
				ue.setGolesContra(golesContra);
				ue.setGolesFavor(golesFavor);
				ue.setGolesDiferencia(golesFavor - golesContra);
				ue.setPartEmpatados(partEmpatados);
				ue.setPartGanados(partGanados);
				ue.setPartPerdidos(partPerdidos);
				ue.setPartJugados(partEmpatados + partGanados + partPerdidos);
				ue.setPuntos((partGanados * 3) + partEmpatados);
				ue.setIdUsuario(user.getId());
				ue.setNombre(user.getNombre());
				boolean noExisteSolicitud = !datos.Solicitud.getInstance().checkSolicitudLiga(usuario.getId(), 
						user.getId(), idLiga);
				boolean noHayPartidosNoRechazados = !datos.Partido.getInstance()
						.checkPartidoRechazado(usuario.getId(), user.getId(), idLiga);
				ue.setPuedeJugar(noExisteSolicitud && noHayPartidosNoRechazados);
				
				estadisticasUsuarios.add(ue);
			}			
			
			estadisticasUsuarios = negocio.UsuarioEstadisticas
					.determinarPosiciones(estadisticasUsuarios);
			
			String report = request.getParameter("report");
			if(report != null && !report.isEmpty()) {
				Reportes generator = new Reportes();
				try {
					negocio.Liga liga = datos.Liga.getInstance().getOne(Integer.parseInt(report));
					ArrayList<negocio.Partido> partidosLiga = datos.Partido.getInstance()
							.getAllPartidosLiga(Integer.parseInt(report));
					String fileName = generator.generarReporteLiga(liga, 
							estadisticasUsuarios, partidosLiga, usuario.getNombre());
					request.setAttribute("name", fileName);
					request.getRequestDispatcher("downloadReporte").forward(request, response);
				} catch(Exception e) {
					Log.getInstance().register(e, "StatsLigaServlet : 133");
				}
			}
			
			boolean ligaTerminada = datos.Liga.getInstance().checkLigaTerminada(idLiga);
			request.setAttribute("liga_terminada", ligaTerminada);
			request.setAttribute("estadisticasUsuarios", estadisticasUsuarios);
			request.setAttribute("idLiga", idLiga);
		} catch (ClassNotFoundException e) {
			Log.getInstance().register(e, "StatsLigaServlet : 142");
		} catch (SQLException e) {
			Log.getInstance().register(e, "StatsLigaServlet : 144");
		}
		request.getRequestDispatcher("estadisticasLiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
