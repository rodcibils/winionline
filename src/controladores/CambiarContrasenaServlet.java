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
 * Servlet implementation class CambiarContrasenaServlet
 */
@WebServlet("/updatePass")
public class CambiarContrasenaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarContrasenaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oldPassword = request.getParameter("oldPassword");
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		
		boolean isValid = true;
		
		if(oldPassword == null || oldPassword.isEmpty()) {
			request.setAttribute("err_opass", "Campo obligatorio");
			isValid = false;
		}
		
		if(password == null || password.isEmpty()) {
			request.setAttribute("err_pass", "Campo obligatorio");
			isValid = false;
		}
		else if(password.length() < 8 || password.length() > 20) {
			request.setAttribute("err_pass", "La contraseña debe tener entre 8 y 20 caracteres");
			isValid = false;
		}
		
		if(repeatedPassword == null || repeatedPassword.isEmpty()) {
			request.setAttribute("err_rpass", "Campo obligatorio");
			isValid = false;
		}
		else if(!password.equals(repeatedPassword)) {
			request.setAttribute("err_rpass", "Las contraseñas no coinciden");
			isValid = false;
		}
		
		if(!isValid) {
			request.setAttribute("old_pass", password);
			request.setAttribute("old_rpass", repeatedPassword);
			request.getRequestDispatcher("editPassword.jsp").forward(request, response);
		} else {
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			datos.Usuario dUsuario = datos.Usuario.getInstance();
			try {
				if(dUsuario.checkPassword(oldPassword, usuario)) {
					usuario.setPassword(password);
					dUsuario.updatePassword(usuario);
					response.sendRedirect("index.jsp?password_changed=true");
				} else {
					request.setAttribute("err_opass", "La contraseña es incorrecta");
					request.getRequestDispatcher("editPassword.jsp").forward(request, response);
				}
			} catch(Exception e) {
				try {
					ConnectionManager.getInstance().closeConnection();
				} catch (SQLException e1) {
					Log.getInstance().register(e, "CambiarContrasenaServlet : 91");
				}
				Log.getInstance().register(e, "CambiarContrasenaServlet : 93");
			}
		}
	}

}
