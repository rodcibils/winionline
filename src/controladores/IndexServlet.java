package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Log;
import utils.Reportes;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		request.setAttribute("data_getted", true);
		try {
			if(usuario.isAdmin()) {
				try {
					int countDisputasVencidas = datos.Disputa.getInstance().getCountDisputasVencidas();
					request.setAttribute("disputas_vencidas", countDisputasVencidas);
					
					int apelacionesAdmin = datos.Apelacion.getInstance()
							.getCountApelacionesAJuzgar(usuario.getId());
					request.setAttribute("apelaciones_admin", apelacionesAdmin);
				} catch(Exception e) {
					Log.getInstance().register(e, "Index Servlet : 46");
				}
			}
			
			int cantSolAmRecPend = datos.Solicitud.getInstance()
					.getCountSolicitudesRecibidasAmistososPendientes(usuario);
			request.setAttribute("sol_am_rec_pend", cantSolAmRecPend);
			
			int cantSolAmEnvPend = datos.Solicitud.getInstance()
					.getCountSolicitudesEnviadasAmistososPendientes(usuario);
			request.setAttribute("sol_am_env_pend", cantSolAmEnvPend);
			
			int amPend = datos.Partido.getInstance().getCountAmistososPendientes(usuario.getId());
			request.setAttribute("am_pend", amPend);
			
			int disputas = datos.Disputa.getInstance().getCountDisputasEnCurso(usuario.getId());
			request.setAttribute("disputas", disputas);
			
			int apelaciones = datos.Apelacion.getInstance().getCountMisApelacionesEnCurso(usuario.getId());
			request.setAttribute("apelaciones", apelaciones);
			
			int inscLigas = datos.Liga.getInstance().getCountLigasActivas(usuario.getId());
			request.setAttribute("insc_ligas", inscLigas);
			
			int cantSolEnvLigaPend = datos.Solicitud.getInstance()
					.getCountSolicitudesEnviadasLigaPendientes(usuario.getId());
			request.setAttribute("liga_enviadas", cantSolEnvLigaPend);
			
			int cantSolRecLigaPend = datos.Solicitud.getInstance()
					.getCountSolicitudesRecibidasLigaPendientes(usuario.getId());
			request.setAttribute("liga_recibidas", cantSolRecLigaPend);
			
			int partLigaPendientes = datos.Partido.getInstance()
					.getPartidosLigaPendientes(usuario.getId());
			request.setAttribute("liga_pendientes", partLigaPendientes);
			
			String reporteUsuario = request.getParameter("reporteUsuario");
			if(reporteUsuario != null && reporteUsuario.contentEquals("true")) {
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance().getLigasUsuario(usuario.getId());
				ArrayList<negocio.UsuarioEstadisticas> stats = new ArrayList<>();
				ArrayList<negocio.Partido> partidosLiga = new ArrayList<>();
				ArrayList<negocio.Partido> amistosos = datos.Partido.getInstance().getAllAmistosos(usuario.getId());
				
				for(negocio.Liga liga : ligas) {
					ArrayList<negocio.Usuario> allUsuarios = datos.Liga.getInstance()
							.getAllUsuariosLiga(liga.getId());
					ArrayList<negocio.UsuarioEstadisticas> allStats = new ArrayList<>();
					
					for(negocio.Usuario curUser : allUsuarios) {
						ArrayList<negocio.Partido> partidos = datos.Partido.getInstance()
								.getPartidosUsuarioLiga(curUser.getId(), liga.getId());
						
						negocio.UsuarioEstadisticas stat = new negocio.UsuarioEstadisticas();
						
						int golesContra = 0;
						int golesFavor = 0;
						int partGanados = 0;
						int partPerdidos = 0;
						int partEmpatados = 0;
						for(negocio.Partido partido : partidos) {
							if(curUser.getNombre().contentEquals(usuario.getNombre()))
							{
								partidosLiga.add(partido);
							}
							
							if(partido.getResultadoUno().getJugador().getNombre().contentEquals(curUser.getNombre())) {
								golesFavor += partido.getResultadoUno().getGoles();
								golesContra += partido.getResultadoDos().getGoles();
								if(partido.getResultadoUno().getGoles() > partido.getResultadoDos().getGoles()) {
									++partGanados;	
								} else if(partido.getResultadoUno().getGoles() < partido.getResultadoDos().getGoles()) {
									++partPerdidos;
								} else {
									++partEmpatados;
								}
							} else {
								golesFavor += partido.getResultadoDos().getGoles();
								golesContra += partido.getResultadoUno().getGoles();
								if(partido.getResultadoUno().getGoles() < partido.getResultadoDos().getGoles()) {
									++partGanados;	
								} else if(partido.getResultadoUno().getGoles() > partido.getResultadoDos().getGoles()) {
									++partPerdidos;
								} else {
									++partEmpatados;
								}
							}
						}
						
						stat.setGolesContra(golesContra);
						stat.setGolesFavor(golesFavor);
						stat.setGolesDiferencia(golesFavor - golesContra);
						stat.setPartJugados(partidos.size());
						stat.setPartGanados(partGanados);
						stat.setPartEmpatados(partEmpatados);
						stat.setPartPerdidos(partPerdidos);
						stat.setPuntos(partGanados * 3 + partEmpatados);
						
						if(curUser.getId() == usuario.getId()) {
							stats.add(stat);
						}
						
						allStats.add(stat);
					}
					
					allStats = negocio.UsuarioEstadisticas.determinarPosiciones(allStats);
					for(int i=0; i<allStats.size(); ++i) {
						allStats.get(i).setPosicion(i + 1);
					}
				}
				
				Reportes reporte = new Reportes();
				String fileName = reporte.generarReporteUsuario(usuario.getNombre() + "-" + 
						usuario.getApodo(), 
						ligas, 
						stats, partidosLiga, 
						amistosos);
				
				request.setAttribute("name", fileName);
				request.getRequestDispatcher("downloadReporte").forward(request, response);
			}
			
		} catch(Exception e) {
			Log.getInstance().register(e, "Index Servlet : 168");
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
