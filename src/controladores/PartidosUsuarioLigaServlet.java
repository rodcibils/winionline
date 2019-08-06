package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class PartidosUsuarioLigaServlet
 */
@WebServlet("/partidosusuarioliga")
public class PartidosUsuarioLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PartidosUsuarioLigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int idLiga = Integer.parseInt(request.getParameter("idliga"));
		int idUsuario = Integer.parseInt(request.getParameter("idusuario"));
		String toSearch = request.getParameter("search");
		if(toSearch != null && !toSearch.contentEquals(lastSearch)) {
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		request.setAttribute("idLiga", idLiga);
		request.setAttribute("idUsuario", idUsuario);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
		
		try {
			if(toSearch == null || toSearch.contentEquals("")) {
				ArrayList<negocio.Partido> partidosUsuarioLiga = datos.Partido.getInstance()
						.getPartidosUsuarioLiga(idUsuario, idLiga, skip, LIMIT);
				
				count = datos.Partido.getInstance().getCountPartidosUsuarioLiga(idUsuario, idLiga);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidosUsuarioLiga", partidosUsuarioLiga);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Partido> partidosUsuarioLiga = datos.Partido.getInstance()
						.getPartidosUsuarioLiga(idUsuario, idLiga, skip, LIMIT, toSearch);
				count = datos.Partido.getInstance().getCountPartidosUsuarioLiga(idUsuario, idLiga, toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidosUsuarioLiga", partidosUsuarioLiga);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		request.getRequestDispatcher("partidosUsuarioLiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
