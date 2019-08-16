package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApelacionesEnCursoServlet
 */
@WebServlet("/misApelacionesEnCurso")
public class ApelacionesEnCursoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApelacionesEnCursoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
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
						.getMisApelacionesEnCurso(usuario.getId(), skip, LIMIT);
				count = datos.Apelacion.getInstance().getCountMisApelacionesEnCurso(usuario.getId());
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
						.getMisApelacionesEnCurso(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Apelacion.getInstance().getCountMisApelacionesEnCurso(usuario.getId(),
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
		
		request.getRequestDispatcher("listApelacionesEnCurso.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
