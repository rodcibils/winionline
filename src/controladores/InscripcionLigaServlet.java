package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InscripcionLigaServlet
 */
@WebServlet("/ligas")
public class InscripcionLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private ArrayList<negocio.Liga> ligas = null;
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
		
//		estado=3 LIGAS PENDIENTES
		try {				
			ligas = datos.Liga.getInstance().getAllByEstado(3);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String inscribir = request.getParameter("inscripcion");
		
		request.setAttribute("ligas", ligas);
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
			if(toSearch == null || toSearch.contentEquals("")) 
			{
				count = datos.Liga.getInstance()
						.getCountLigasByEstado(3);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAllPaginadoByEstado(skip,LIMIT,3);
				request.setAttribute("ligas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else 
			{
				count = datos.Liga.getInstance()
						.getCountLigasFilteredByEstado(toSearch, 3);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getLigasPaginationByEstado(toSearch, skip, LIMIT, 3);
				request.setAttribute("usuarios", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
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
