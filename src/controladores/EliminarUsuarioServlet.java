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
 * Servlet implementation class EliminarUsuarioServlet
 */
@WebServlet("/deleteUser")
public class EliminarUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarUsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		try {
			datos.Solicitud.getInstance().cleanupSolicitudes(usuario.getId());
			datos.Usuario.getInstance().delete(usuario);
			request.getSession().setAttribute("usuario", null);
			response.sendRedirect("login.jsp?delete_success=true");
		}catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "EliminarUsuarioServlet : 44");
			}
			Log.getInstance().register(e, "EliminarUsuarioServlet : 46");
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
