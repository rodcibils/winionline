package controladores;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datos.ConnectionManager;
import utils.Log;

/**
 * Servlet implementation class AceptarPartidoLigaServlet
 */
@WebServlet("/aceptarPartidoLiga")
public class AceptarPartidoLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AceptarPartidoLigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idSolicitud = request.getParameter("solicitud");
		int id = Integer.parseInt(idSolicitud);
		try {
			datos.Solicitud.getInstance().aceptarSolicitud(id);
			datos.Partido.getInstance().createPartido(id);
			request.setAttribute("accept_success", true);
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "AceptarPartidoLigaServlet : 44");
			}
			Log.getInstance().register(e, "AceptarPartidoLigaServlet : 46");
		}
		
		request.getRequestDispatcher("solicitudesRecibidasLiga?skip=0").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
