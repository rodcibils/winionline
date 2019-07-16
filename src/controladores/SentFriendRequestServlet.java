package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SentFriendRequestServlet
 */
@WebServlet("/sentFriendRequest")
public class SentFriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SentFriendRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		int skip = Integer.parseInt(request.getParameter("skip"));
		try {
			datos.Solicitud.getInstance().cleanupSolicitudesEnviadasAmistososPendientes(usuario);
			int count = datos.Solicitud.getInstance().getCountSolicitudesEnviadasAmistososPendientes(usuario);
			int maxPages = count / LIMIT;
			if(count % LIMIT != 0) {
				++maxPages;
			}
			
			int currentPage = skip / LIMIT;
			
			ArrayList<negocio.Solicitud> solicitudes = datos.Solicitud.getInstance()
					.getSolicitudesEnviadasAmistososPendientes(usuario, skip, LIMIT);
			request.setAttribute("solicitudes", solicitudes);
			request.setAttribute("skip", skip);
			request.setAttribute("current_page", currentPage);
			request.setAttribute("max_pages", maxPages);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listSentFriendlyMatchRequest.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
