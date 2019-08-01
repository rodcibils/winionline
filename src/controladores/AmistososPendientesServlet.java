package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AmistososPendientesServlet
 */
@WebServlet("/pendingFriendlyMatch")
public class AmistososPendientesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AmistososPendientesServlet() {
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
			request.setAttribute("id_partido", sRegister);
			request.setAttribute("coming_from", "pendingFriendlyMatch?skip=0");
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
				request.setAttribute("friendly_match_rejected", true);
				if((count - 1) % LIMIT == 0 && skip != 0) {
					skip -= 10;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
			if(toSearch == null || toSearch.contentEquals("")) {
				ArrayList<negocio.Partido> amistososPendientes = datos.Partido.getInstance()
						.getAmistososPendientes(usuario.getId(), skip, LIMIT);
				count = amistososPendientes.size();
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("amistosos", amistososPendientes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Partido> amistososPendientes = datos.Partido.getInstance()
						.getAmistososPendientes(usuario.getId(), skip, LIMIT, toSearch);
				count = amistososPendientes.size();
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("amistosos", amistososPendientes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listPendingFriendlyMatch.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
