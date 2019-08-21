package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApelacionesAJuzgarServlet
 */
@WebServlet("/apelacionesAJuzgar")
public class ApelacionesAJuzgarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApelacionesAJuzgarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String votar = request.getParameter("vote");
		if(votar != null && !votar.isEmpty()) {
			String idJugador = request.getParameter("jugador");
			try {
				int parsedId = Integer.parseInt(votar);
				datos.Apelacion.getInstance().votarApelacion(usuario.getId(), parsedId, 
						Integer.parseInt(idJugador));
				
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
					datos.Partido.getInstance().finalizarPartido(parsedId, usuario.getId());
				}
				
				request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
				if((count - 1) % LIMIT == 0 && skip != 0) skip -= LIMIT;
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		String toSearch = request.getParameter("search");
		if(toSearch != null && !toSearch.contentEquals(lastSearch)) {
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
		
		try {
			if(toSearch == null || toSearch.isEmpty()) {
				ArrayList<negocio.Apelacion> apelaciones = datos.Apelacion.getInstance()
						.getApelacionesAJuzgar(usuario.getId(), skip, LIMIT);
				count = datos.Apelacion.getInstance().getCountApelacionesAJuzgar(usuario.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				for(negocio.Apelacion apelacion : apelaciones) {
					int votosUno = datos.Disputa.getInstance()
							.getVotos(apelacion.getDisputa().getPartido().getId(), 
							apelacion.getDisputa().getPartido().getResultadoUno().getJugador().getId());
					int votosDos = datos.Disputa.getInstance()
							.getVotos(apelacion.getDisputa().getPartido().getId(), 
							apelacion.getDisputa().getPartido().getResultadoDos().getJugador().getId());
					apelacion.getDisputa().setVotosUno(votosUno);
					apelacion.getDisputa().setVotosDos(votosDos);
				}
				
				request.setAttribute("apelaciones", apelaciones);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Apelacion> apelaciones = datos.Apelacion.getInstance()
						.getApelacionesAJuzgar(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Apelacion.getInstance().getCountApelacionesAJuzgar(usuario.getId(),
						toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				for(negocio.Apelacion apelacion : apelaciones) {
					int votosUno = datos.Disputa.getInstance()
							.getVotos(apelacion.getDisputa().getPartido().getId(), 
							apelacion.getDisputa().getPartido().getResultadoUno().getJugador().getId());
					int votosDos = datos.Disputa.getInstance()
							.getVotos(apelacion.getDisputa().getPartido().getId(), 
							apelacion.getDisputa().getPartido().getResultadoDos().getJugador().getId());
					apelacion.getDisputa().setVotosUno(votosUno);
					apelacion.getDisputa().setVotosDos(votosDos);
				}
				
				request.setAttribute("apelaciones", apelaciones);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listApelacionesAJuzgar.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
