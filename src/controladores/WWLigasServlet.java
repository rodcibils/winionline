package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Log;

/**
 * Servlet implementation class WWLigasServlet
 */
@WebServlet("/wwligas")
public class WWLigasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.Liga> ligas = null;
	private static final int LIMIT = 10;
	private int skip = 0;
	private String lastSearch = "";
	private int lastYearSearch = 0;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WWLigasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try 
		{
			List<Integer> temporadas = datos.Liga.getInstance().getTemporadas();
			request.setAttribute("temporadas", temporadas);
			
			String action = request.getParameter("action");
			if (action != null && action.equals("eliminar"))
			{
				int id = Integer.parseInt(request.getParameter("id"));
				datos.Liga.getInstance().delete(id);
			}
			
			String toSearch = request.getParameter("search");
			
			int yearSearch;
			if (request.getParameter("yearSearch") == null)
				yearSearch = 0;
			else
				yearSearch = Integer.parseInt(request.getParameter("yearSearch"));
			
			if(toSearch!=null && !toSearch.contentEquals(lastSearch)){
				lastSearch = toSearch;
				skip = 0;
			}
			
			if(yearSearch != lastYearSearch){
				lastYearSearch = yearSearch;
				skip = 0;
			}
			
			int count = 0;
			String sSkip = request.getParameter("skip");
			if(sSkip != null) {
				skip = Integer.parseInt(sSkip);
			}
			if((toSearch == null || toSearch.contentEquals("")) && (yearSearch == 0))
			{
				ligas = datos.Liga.getInstance().getAllPaginado(skip, LIMIT);
				count = datos.Liga.getInstance().getCountLigas();
			}
			else
			{
				ligas = datos.Liga.getInstance().getAllPaginado(toSearch, yearSearch, skip, LIMIT);
				count = datos.Liga.getInstance().getCountLigas(toSearch, yearSearch);
			}
			request.setAttribute("ligas", ligas);
			request.setAttribute("skip", skip);
			int maxPages = count / LIMIT;
			if(count % LIMIT != 0) {
				++maxPages;
			}
			
			int currentPage = skip / LIMIT;
			request.setAttribute("search", lastSearch);
			request.setAttribute("year", lastYearSearch);
			request.setAttribute("current_page", currentPage);
			request.setAttribute("max_pages", maxPages);
			request.setAttribute("count", count);
			
			request.setAttribute("ligas", ligas);
			request.getRequestDispatcher("wwleague.jsp").forward(request, response);
			
		} catch (ClassNotFoundException | SQLException e) {
			Log.getInstance().register(e, "WWLigasServlet : 103");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
