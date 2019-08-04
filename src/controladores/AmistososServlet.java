package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AmistososServlet
 */
@WebServlet("/listFriendlyMatches")
public class AmistososServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AmistososServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String sEdit = request.getParameter("edit");
		if(sEdit != null && !sEdit.isEmpty()) {
			try {
				negocio.Partido partido = datos.Partido.getInstance().getOne(Integer.parseInt(sEdit));
				Calendar c = Calendar.getInstance();
				c.setTime(partido.getFecha());
				c.add(Calendar.DATE, 5);
				Date today = new Date();
				if(today.after(c.getTime())) {
					request.setAttribute("err_edit", "Han pasado mas de 5 dias desde la fecha del "
							+ "partido");
				} else {
					request.setAttribute("id", sEdit);
					request.setAttribute("mode", "edit");
					request.setAttribute("coming_from", "listFriendlyMatches?skip=" + skip + "&search="
							+ lastSearch);
					request.getRequestDispatcher("registerMatchResult").forward(request, response);
					return;
				}
			} catch(Exception e) {
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
			if(toSearch == null || toSearch.contentEquals("")) {
				ArrayList<negocio.Partido> amistosos = datos.Partido.getInstance()
						.getAmistosos(usuario.getId(), skip, LIMIT);
				
				count = datos.Partido.getInstance().getCountAmistosos(usuario.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("amistosos", amistosos);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Partido> amistosos = datos.Partido.getInstance()
						.getAmistosos(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Partido.getInstance().getCountAmistosos(usuario.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("amistosos", amistosos);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listAmistosos.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
