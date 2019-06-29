package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrarUsuarioServlet
 */
@WebServlet("/register")
public class RegistrarUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.Pais> paises = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrarUsuarioServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			paises = datos.Pais.getInstance().getAll();
			request.setAttribute("paises", paises);
			request.getRequestDispatcher("register.jsp").forward(request, response);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nombre = request.getParameter("username");
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		String birthdate = request.getParameter("birthdate");
		String email = request.getParameter("email");
		String apodo = request.getParameter("apodo");
		String nombre_pais = request.getParameter("pais");
		String skype = request.getParameter("skype");
		String ip = request.getParameter("ip");
		//TODO: Ver como es el tema de subir un avatar desde la pc del usuario
		String avatar = request.getParameter("avatar");
		
		boolean isValid = true;
		
		if(nombre == null || nombre.isEmpty()){
			request.setAttribute("err_nombre", "Campo obligatorio");
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
		
		Date fechanac = null;
		if(birthdate!= null) {
			try {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				fechanac = df.parse(birthdate);
				request.setAttribute("old_date", birthdate);
			} catch(ParseException e) {
				request.setAttribute("err_date", "La fecha debe tener formato dd/MMMM/yyyy");
				isValid = false;
			}
		}
		
		if(email!=null && (!email.contains("@") || !email.contains("."))) {
			request.setAttribute("err_mail", "El mail ingresado no es válido");
			isValid = false;
		}
		
		if(!isValid) {
			request.setAttribute("old_nombre", nombre);
			request.setAttribute("old_pass", password);
			request.setAttribute("old_rpass", repeatedPassword);
			request.setAttribute("old_birthdate", birthdate);
			request.setAttribute("old_email", email);
			request.setAttribute("old_apodo", apodo);
			request.setAttribute("old_skype", skype);
			request.setAttribute("old_ip", ip);
			request.setAttribute("paises", paises);
			request.getRequestDispatcher("register.jsp").forward(request, response);
		} else {
			negocio.Usuario usuario = new negocio.Usuario();
			usuario.setNombre(nombre);
			usuario.setPassword(password);
			usuario.setApodo(apodo);
			usuario.setFechanac(fechanac);
			usuario.setEmail(email);
			usuario.setSkype(skype);
			usuario.setIp(ip);
			for(negocio.Pais pais : paises) {
				if(pais.getNombre().equals(nombre_pais)) {
					usuario.setPais(pais);
					break;
				}
			}
			//TODO: reemplazar cuando se implemente lo del avatar
			usuario.setAvatar("prueba");
			datos.Usuario dUsuario = datos.Usuario.getInstance();
			try {
				if(!dUsuario.checkIfUserExists(usuario.getNombre())) {
					dUsuario.insert(usuario);
					response.sendRedirect("login.jsp");
				} else {
					request.setAttribute("err_nombre", "Ya existe un usuario con ese nombre");
					request.setAttribute("old_nombre", nombre);
					request.setAttribute("old_pass", password);
					request.setAttribute("old_rpass", repeatedPassword);
					request.setAttribute("old_birthdate", birthdate);
					request.setAttribute("old_email", email);
					request.setAttribute("old_apodo", apodo);
					request.setAttribute("old_skype", skype);
					request.setAttribute("old_ip", ip);
					request.setAttribute("paises", paises);
					request.getRequestDispatcher("register.jsp").forward(request, response);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
