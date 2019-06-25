package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import negocio.Usuario;
import util.Mensajes;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
		ArrayList<String> mensajes = (ArrayList<String>)sesion.getAttribute("mensajes");
        sesion.setAttribute("mensajes", new ArrayList<String>());
        
        request.setAttribute("mensajes", mensajes);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        datos.Usuario datosUsuario = datos.Usuario.getInstance();
//        
        Mensajes mensajes = new Mensajes();
        HttpSession sesion = request.getSession();
        sesion.setAttribute("mensajes", mensajes);
        
        try {
        	// autenticar usuario
            Usuario user = new Usuario();//= datosUsuario.autenticate(request.getParameter("userEmal"), request.getParameter("userPassword"));
            if(user != null) {
                sesion.setAttribute("usuario", user);
                
                // ver roles
//                negocio.Rol rol = datosRol.getOne(user.getId_rol());
//                sesion.setAttribute("rol", rol.getNombre());
                
                response.sendRedirect(request.getContextPath());
                return;
            }
        } catch(Exception e) {
            e.printStackTrace();
            mensajes.add("Hubo un problema en la autenticacion", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

}
