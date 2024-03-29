package controladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datos.ConnectionManager;
import negocio.Evidencia;
import utils.Log;
import utils.Utils;

/**
 * Servlet implementation class EvidenciaServlet
 */
@WebServlet("/evidencia")
public class EvidenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EvidenciaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idDisputa = request.getParameter("id");
		String idJugador = request.getParameter("jugador");
		
		String votar = request.getParameter("vote");
		if(votar != null && !votar.isEmpty()) {
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			try {
				if(datos.Disputa.getInstance().checkApelacion(Integer.parseInt(votar))) {
					int parsedId = Integer.parseInt(votar);
					datos.Apelacion.getInstance().votarApelacion(usuario.getId(),
							parsedId, Integer.parseInt(idJugador));
					
					//chequeo para cerrar apelacion
					int votos = datos.Apelacion.getInstance().getCountVotos(parsedId);
					if(votos == negocio.Apelacion.CANT_JUECES) {
						datos.Apelacion.getInstance().cerrar(parsedId);
						datos.Disputa.getInstance().cerrarDisputa(parsedId);
						
						ArrayList<negocio.Usuario> jugadores = datos.Apelacion.getInstance()
								.getJugadores(parsedId);
						int votosUno = datos.Apelacion.getInstance().getVotos(parsedId, 
								jugadores.get(0).getId());
						int votosDos = datos.Apelacion.getInstance().getVotos(parsedId, 
								jugadores.get(1).getId());
						
						negocio.Resultado resultadoUno = new negocio.Resultado();
						resultadoUno.setJugador(jugadores.get(0));
						negocio.Partido partido = new negocio.Partido();
						partido.setId(parsedId);
						resultadoUno.setPartido(partido);
						negocio.Resultado resultadoDos = new negocio.Resultado();
						resultadoDos.setPartido(partido);
						resultadoDos.setJugador(jugadores.get(1));
						if(votosUno > votosDos) {
							resultadoUno.setGoles(3);
							resultadoDos.setGoles(0);
						} else if(votosUno < votosDos) {
							resultadoUno.setGoles(0);
							resultadoDos.setGoles(3);
						} else {
							resultadoUno.setGoles(0);
							resultadoDos.setGoles(0);
						}
						
						datos.Resultado.getInstance().update(resultadoUno);
						datos.Resultado.getInstance().update(resultadoDos);
					}
					
					request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
					request.getRequestDispatcher("apelacionesAJuzgar?skip=0&search=")
						.forward(request, response);
					return;
				} else {
					datos.Disputa.getInstance().votarDisputa(usuario.getId(), 
							Integer.parseInt(votar), Integer.parseInt(idJugador));
					request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
					request.getRequestDispatcher("listDisputas?skip=0&search=").forward(request, response);
					return;
				}
			} catch(Exception e) {
				try {
					ConnectionManager.getInstance().closeConnection();
				} catch (SQLException e1) {
					Log.getInstance().register(e, "EvidenciaServlet : 105");
				}
				Log.getInstance().register(e, "EvidenciaServlet : 107");
			}
		}
		
		ArrayList<Evidencia> evidencias = new ArrayList<>();
		try {
			negocio.Disputa disputa = datos.Disputa.getInstance().getOne(Integer.parseInt(idDisputa));
			negocio.Usuario jugador = datos.Usuario.getInstance().getOne(Integer.parseInt(idJugador));
			String path = datos.Parametro.getInstance().getEvidenciasPath();
			File folder = new File(path + "/" + idDisputa + "/" + idJugador);
			if(folder.exists()) {
				for(File file : folder.listFiles()) {
					Evidencia evidencia = new Evidencia();
					evidencia.setFecha(new Date(file.lastModified()));
					evidencia.setPath("/" + idDisputa + "/" + idJugador + "/" + file.getName());
					if(Utils.isImageFile(file.getPath())) {
						evidencia.setTipo(Evidencia.IMAGEN);
					} else {
						evidencia.setTipo(Evidencia.VIDEO);
						try {
							FileReader reader = new FileReader(file.getPath());
							BufferedReader buffer = new BufferedReader(reader);
							String line;
							while((line = buffer.readLine()) != null) {
								String embedLine = line.split("&")[0].replace("watch?v=", "embed/");
								evidencia.setLink(embedLine);
							}
							buffer.close();
						}catch(Exception e) {
							Log.getInstance().register(e, "EvidenciaServlet : 129");
						}
					}
					evidencias.add(evidencia);
				}
			}
				
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			request.setAttribute("jugador", jugador.getNombre() + " - " + jugador.getApodo());
			request.setAttribute("fecha_partido", sdf.format(disputa.getPartido().getFecha()));
			request.setAttribute("vencimiento", sdf.format(disputa.getVencimiento()));
			request.setAttribute("resultado_partido" , disputa.getPartido().getResultadoUno()
					.getGoles() + " - " + disputa.getPartido().getResultadoDos().getGoles());
			if(disputa.getPartido().getResultadoUno().getJugador().getId() 
					== Integer.parseInt(idJugador))
			{
				request.setAttribute("rival", disputa.getPartido().getResultadoUno()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoUno()
						.getJugador().getApodo());
			} else {
				request.setAttribute("rival", disputa.getPartido().getResultadoDos()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoDos()
						.getJugador().getApodo());
			}
			request.setAttribute("evidencias", evidencias);
		}catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "EvidenciaServlet : 166");
			}
			Log.getInstance().register(e, "EvidenciaServlet : 168");
		}
		
		request.setAttribute("id_disputa", idDisputa);
		request.setAttribute("id_jugador", idJugador);
		request.setAttribute("count", evidencias.size());
		request.getRequestDispatcher("verEvidenciasDisputa.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
