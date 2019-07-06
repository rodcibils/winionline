package controladores;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		
		boolean isValid = true;
		
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
			usuario.setPassword(password);
			datos.Usuario dUsuario = datos.Usuario.getInstance();
			try {
				dUsuario.updatePassword(usuario);
				response.sendRedirect("index.jsp");
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
