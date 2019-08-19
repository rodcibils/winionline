package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReceivedFriendRequestServlet
 */
@WebServlet("/receivedFriendRequest")
public class ReceivedFriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceivedFriendRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
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
		
		String sDelete = request.getParameter("delete");
		if(sDelete != null) {
			try {
				datos.Solicitud.getInstance().delete(Integer.parseInt(sDelete));
				request.setAttribute("friendly_sol_deleted", true);
				if((count - 1) % LIMIT == 0 && skip != 0) {
					skip -= LIMIT;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
			if(toSearch == null || toSearch.contentEquals("")) {
				count = datos.Solicitud.getInstance()
						.getCountSolicitudesRecibidasAmistososPendientes(usuario);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Solicitud> solicitudes = datos.Solicitud.getInstance()
						.getSolicitudesRecibidasAmistososPendientes(usuario, skip, LIMIT);
				request.setAttribute("solicitudes", solicitudes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				count = datos.Solicitud.getInstance()
						.getCountSolicitudesRecibidasAmistososPendientes(usuario, toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Solicitud> solicitudes = datos.Solicitud.getInstance()
						.getSolicitudesRecibidasAmistososPendientes(usuario, skip, LIMIT, toSearch);
				request.setAttribute("solicitudes", solicitudes);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listReceivedFriendlyMatchRequest.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
