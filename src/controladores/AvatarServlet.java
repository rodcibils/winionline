package controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/showAvatar")
public class AvatarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AvatarServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String idUsuario = request.getParameter("id_usuario");
		negocio.Usuario usuario = null;
		
		if(idUsuario == null || idUsuario.isEmpty()) {
			usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		} else {
			try {
				usuario = datos.Usuario.getInstance().getOne(Integer.parseInt(idUsuario));
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		response.setContentType("image");
		String avatarPath = usuario.getAvatar();
		if(avatarPath == null) avatarPath = this.getServletContext().getRealPath("Resources/avatar/default_avatar.jpeg");
		File file = new File(avatarPath);
		response.setContentLength((int)file.length());
		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		byte[] buf = new byte[1024];
		int count = 0;
		while((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		
		out.close();
		in.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
