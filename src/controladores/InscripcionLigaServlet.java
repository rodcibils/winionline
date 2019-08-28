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
 * Servlet implementation class InscripcionLigaServlet
 */
@WebServlet("/ligas")
public class InscripcionLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InscripcionLigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		negocio.Usuario usuarioActual = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String action = request.getParameter("action");
		
		if(action != null && action.equals("inscribir"))
		{
			try {
				int idLiga = Integer.parseInt(request.getParameter("id"));
				datos.Liga.getInstance().inscribir(usuarioActual.getId(), idLiga);
				request.setAttribute("inscripto", "Se ha inscripto a la liga correctamente");
			} catch (ClassNotFoundException | SQLException e) {
				try {
					ConnectionManager.getInstance().closeConnection();
				} catch (SQLException e1) {
					Log.getInstance().register(e, "InscripcionLigaServlet : 54");
				}
				Log.getInstance().register(e, "InscripcionLigaServlet : 56");
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
				count = datos.Liga.getInstance().getCountPendientes(usuarioActual.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAllPendientes(usuarioActual.getId(), skip, LIMIT);
				request.setAttribute("ligas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else 
			{
				count = datos.Liga.getInstance().getCountPendientes(usuarioActual.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAllPendientes(usuarioActual.getId(), toSearch, skip, LIMIT);
				request.setAttribute("ligas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "InscripcionLigaServlet : 112");
			}
			Log.getInstance().register(e, "InscripcionLigaServlet : 114");
		}
		
		request.getRequestDispatcher("inscripcionALiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}

}
