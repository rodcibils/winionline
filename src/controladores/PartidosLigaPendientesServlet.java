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
 * Servlet implementation class PartidosLigaPendientesServlet
 */
@WebServlet("/partidosPendientesLiga")
public class PartidosLigaPendientesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PartidosLigaPendientesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String sRegister = request.getParameter("register");
		if(sRegister != null && !sRegister.isEmpty()){
			request.setAttribute("id", sRegister);
			request.setAttribute("coming_from", "partidosPendientesLiga?skip=0");
			request.setAttribute("mode", "new");
			request.getRequestDispatcher("registerMatchResult").forward(request, response);
			return;
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
		
		String sReject = request.getParameter("reject");
		if(sReject != null) {
			try {
				datos.Partido.getInstance().reject(Integer.parseInt(sReject));
				request.setAttribute("match_rejected", true);
				if((count - 1) % LIMIT == 0 && skip != 0) {
					skip -= 10;
				}
			} catch(Exception e) {
				try {
					ConnectionManager.getInstance().closeConnection();
				} catch (SQLException e1) {
					Log.getInstance().register(e, "PartidosLigaPendientesServlet : 74");
				}
				Log.getInstance().register(e, "PartidosLigaPendientesServlet : 76");
			}
		}
		
		try {
			if(toSearch == null || toSearch.isEmpty()) {
				ArrayList<negocio.Partido> partidosPendientes = datos.Partido.getInstance()
						.getPartidosPendientes(usuario.getId(), skip, LIMIT);
				count = datos.Partido.getInstance().getCountPartidosPendientes(usuario.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidos", partidosPendientes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Partido> partidosPendientes = datos.Partido.getInstance()
						.getPartidosPendientes(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Partido.getInstance().getCountPartidosPendientes(usuario.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidos", partidosPendientes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "PartidosLigaPendientesServlet : 112");
			}
			Log.getInstance().register(e, "PartidosLigaPendientesServlet : 114");
		}
		
		request.getRequestDispatcher("listPartidosPendientesLiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
