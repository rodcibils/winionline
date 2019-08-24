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

import utils.Log;
import utils.Utils;

/**
 * Servlet implementation class DownloadEvidenciaServlet
 */
@WebServlet("/downloadEvidencia")
public class DownloadEvidenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadEvidenciaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String path = datos.Parametro.getInstance().getEvidenciasPath() + 
					request.getParameter("path");
			String ext = Utils.getFileExtension(path);
			response.setContentType("image/" + ext);
			response.setHeader("Content-disposition", "attachment; filename=evidencia." + ext);
			File file = new File(path);
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			int length;
			while((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
		} catch (Exception e) {
			Log.getInstance().register(e, "DownloadEvidenciaServlet : 53");
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
