package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import negocio.Usuario;

/**
 * Servlet implementation class LoginUsuarioServlet
 */
@WebServlet("/login")
public class LoginUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUsuarioServlet() {
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
		// TODO Auto-generated method stub
		doGet(request, response);
		
		// variable de sesion
		HttpSession sesion = request.getSession();
		
		// agarro el nombre y contraseña del login
		datos.Usuario dUsuario = datos.Usuario.getInstance();
		String email = request.getParameter("userEmail");
		String password = request.getParameter("userPassword");
		
		boolean isValid = true;
		
		// valido que haya ingresado usuario y contraseña
		if(email == null || email.isEmpty()){
			request.setAttribute("err_email", "Debe ingresar usuario");
			isValid = false;
		}	
		if(password == null || password.isEmpty()) {
			request.setAttribute("err_pass", "Debe ingresar contraseña");
			isValid = false;
		}
		
		if(!isValid) {
			request.setAttribute("old_nombre", email);
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			try {
				Usuario user = dUsuario.autenticate(email, password);
				if(user != null) {
	                sesion.setAttribute("usuario", user);
	                response.sendRedirect("index.jsp");
	                return;
	            }
				else {
					request.setAttribute("err_email", "Usuario y/o contraseña inválida");
					request.setAttribute("old_nombre", email);
					request.getRequestDispatcher("login.jsp").forward(request, response);
				}
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
