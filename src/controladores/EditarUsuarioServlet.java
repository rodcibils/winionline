package controladores;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import utils.Utils;

/**
 * Servlet implementation class EditarUsuarioServlet
 */
@WebServlet("/editUser")
@MultipartConfig
public class EditarUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.Pais> paises = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditarUsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			paises = datos.Pais.getInstance().getAll();
			request.setAttribute("paises", paises);
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			String sDate = new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechanac());
			request.setAttribute("old_date", sDate);
			if(usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
				request.setAttribute("has_avatar", "true");
			}
			request.getRequestDispatcher("editUser.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String birthdate = request.getParameter("birthdate");
		String email = request.getParameter("email");
		String apodo = request.getParameter("apodo");
		String nombre_pais = request.getParameter("pais");
		String skype = request.getParameter("skype");
		String ip = request.getParameter("ip");
		
		boolean isValid = true;
		Part filePart = request.getPart("avatar");
		String fileName = Paths.get(filePart.getSubmittedFileName()).toString();
		if(fileName != null && !fileName.isEmpty()) {
			if(!fileName.contains(".jpg") && !fileName.contains(".jpeg") && 
					!fileName.contains(".png")) 
			{
				request.setAttribute("err_avatar", "El formato de imagen cargado no es valido");
				isValid = false;
			} else {
				try(InputStream stream = filePart.getInputStream()){
					Image image = ImageIO.read(stream);
					int width = image.getWidth(null);
					int height = image.getHeight(null);
					if(width > 500 || height > 500) {
						isValid = false;
						request.setAttribute("err_avatar", 
								"El tamaño maximo permitido para el avatar es de 500x500");
					}
				} catch (Exception e) {
					isValid = false;
					request.setAttribute("err_avatar", "Error al subir la imagen al servidor");
				}
			}
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
			request.setAttribute("old_birthdate", birthdate);
			request.setAttribute("old_email", email);
			request.setAttribute("old_apodo", apodo);
			request.setAttribute("old_skype", skype);
			request.setAttribute("old_ip", ip);
			request.setAttribute("paises", paises);
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			if(usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
				request.setAttribute("has_avatar", "true");
			}
			request.getRequestDispatcher("editUser.jsp").forward(request, response);
		} else {
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
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
			datos.Usuario dUsuario = datos.Usuario.getInstance();
			try {
				if(fileName != null && !fileName.isEmpty()) {
					String fileExt = Utils.getFileExtension(fileName);
					if(usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
						File old_file = new File(usuario.getAvatar());
						old_file.delete();
					}
					File file = new File(dUsuario.getParametroPath(),
							usuario.getNombre() + "." + fileExt);
					try(InputStream stream = filePart.getInputStream()){
						Files.copy(stream, file.toPath());
						usuario.setAvatar(file.toPath().toString());
					} catch(Exception e) {
						System.out.println(e.getMessage());
					}
				}
				dUsuario.update(usuario);
				request.getSession().setAttribute("usuario", usuario);
				response.sendRedirect("index.jsp");
			} catch(Exception e) {
				System.out.print(e.getMessage());
			}
		}
	}

}
