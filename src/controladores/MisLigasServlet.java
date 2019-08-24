package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Log;

/**
 * Servlet implementation class MisLigasServlet
 */
@WebServlet("/misLigas")
public class MisLigasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MisLigasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuarioActual = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String unsuscribe = request.getParameter("unsuscribe");
		if(unsuscribe != null && !unsuscribe.isEmpty()) {
			try {
				datos.Liga.getInstance().desinscribirse(usuarioActual.getId(), 
						Integer.parseInt(unsuscribe));
				if((count - 1) % LIMIT == 0 && skip != 0) skip -= LIMIT;
				request.setAttribute("unsuscribe_success", "Se ha removido correctamente la inscripcion a la liga");
			} catch(Exception e) {
				Log.getInstance().register(e, "MisLigasServlet : 47");
			}
		}
		
		String toSearch = request.getParameter("search");
		if(toSearch!=null && !toSearch.contentEquals(lastSearch)){
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
	
		try {
			if(toSearch == null || toSearch.isEmpty()) 
			{
				count = datos.Liga.getInstance().getCount(usuarioActual.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAll(usuarioActual.getId(), skip, LIMIT);
				request.setAttribute("misLigas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else 
			{
				count = datos.Liga.getInstance()
						.getCount(usuarioActual.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAll(usuarioActual.getId(), toSearch, skip, LIMIT);
				request.setAttribute("misLigas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			Log.getInstance().register(e, "MisLigasServlet : 100");
		}
		
		request.getRequestDispatcher("misLigas.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
