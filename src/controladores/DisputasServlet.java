package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datos.ConnectionManager;
import utils.Log;

/**
 * Servlet implementation class DisputasServlet
 */
@WebServlet("/listDisputas")
public class DisputasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisputasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		try {
			if(datos.Partido.getInstance().getCountPartidosJugados(usuario.getId()) 
					< negocio.Disputa.ANTIGUEDAD_PARA_VOTAR)
			{
				request.setAttribute("err_vote", "Debe tener al menos " 
						+ negocio.Disputa.ANTIGUEDAD_PARA_VOTAR  + " partidos jugados "
						+ "para poder votar en las disputas");
				request.getRequestDispatcher("index").forward(request, response);
			}
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "DisputasServlet : 54");
			}
			Log.getInstance().register(e, "DisputasServlet : 56");
		}
		
		String toSearch = request.getParameter("search");
		if(toSearch != null && !toSearch.contentEquals(lastSearch)) {
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		
		String votar = request.getParameter("vote");
		if(votar != null && !votar.isEmpty()) {
			String idJugador = request.getParameter("jugador");
			try {
				datos.Disputa.getInstance().votarDisputa(usuario.getId(), 
						Integer.parseInt(votar), Integer.parseInt(idJugador));
				request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
				if((count - 1) % LIMIT == 0 && skip != 0) skip -= LIMIT;
			}catch(Exception e) {
				try {
					ConnectionManager.getInstance().closeConnection();
				} catch (SQLException e1) {
					Log.getInstance().register(e, "DisputasServlet : 78");
				}
				Log.getInstance().register(e, "DisputasServlet : 80");
			}
		}
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null && !sSkip.isEmpty()) {
			skip = Integer.parseInt(sSkip);
		}
		
		try {
			if(toSearch == null || toSearch.isEmpty()) {
				ArrayList<negocio.Disputa> disputas = datos.Disputa.getInstance()
						.getAll(usuario.getId(), skip, LIMIT);
				count = datos.Disputa.getInstance().getAllCount(usuario.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("disputas", disputas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Disputa> disputas = datos.Disputa.getInstance()
						.getAll(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Disputa.getInstance().getAllCount(usuario.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("disputas", disputas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "DisputasServlet : 121");
			}
			Log.getInstance().register(e, "DisputasServlet : 123");
		}
		
		request.getRequestDispatcher("listDisputas.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
