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
		try {
			int idLiga = Integer.parseInt(request.getParameter("id"));
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
						if (golesJugadorUsuario > golesJugadorRival)
							partGanados ++;
						else
							if (golesJugadorRival > golesJugadorUsuario)
								partPerdidos ++;
							else
								partEmpatados ++;
					}
				}
				
				ue.setGolesContra(golesContra);
				ue.setGolesFavor(golesFavor);
				ue.setGolesDiferencia(golesFavor-golesContra);
				ue.setPartEmpatados(partEmpatados);
				ue.setPartGanados(partGanados);
				ue.setPartPerdidos(partPerdidos);
				ue.setPartJugados(partEmpatados + partGanados+partPerdidos);
				ue.setPuntos((partGanados * 3) + partEmpatados);
				ue.setIdUsuario(user.getId());
				ue.setNombre(user.getNombre());
				estadisticasUsuarios.add(ue);
			}			
			
			estadisticasUsuarios = negocio.UsuarioEstadisticas
					.determinarPosiciones(estadisticasUsuarios);
			request.setAttribute("estadisticasUsuarios", estadisticasUsuarios);
			request.setAttribute("idLiga", idLiga);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
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
