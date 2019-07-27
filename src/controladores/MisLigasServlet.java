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
 * Servlet implementation class MisLigasServlet
 */
@WebServlet("/misLigas")
public class MisLigasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.Liga> ligas = null;
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
		// TODO Auto-generated method stub
		negocio.Usuario usuarioActual = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
//		estado=3 LIGAS PENDIENTES
		try {				
			ligas = datos.Liga.getInstance().getAllByUsuario(usuarioActual.getId());
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
						.getCountLigasByUsuario(usuarioActual.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getAllPaginadoByUsuario(skip,LIMIT,usuarioActual.getId());
				request.setAttribute("misLigas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else 
			{
				count = datos.Liga.getInstance()
						.getCountLigasFilteredByUsuario(toSearch, usuarioActual.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Liga> ligas = datos.Liga.getInstance()
						.getLigasPaginationByUsuario(toSearch, skip, LIMIT, usuarioActual.getId());
				request.setAttribute("misLigas", ligas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
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
