package controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datos.ConnectionManager;
import utils.Log;

/**
 * Servlet implementation class DownloadReporteServlet
 */
@WebServlet("/downloadReporte")
public class DownloadReporteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadReporteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String reportName = (String)request.getAttribute("name");
			String path = datos.Parametro.getInstance().getReportesPath();
			path += "/" + reportName;
			File file = new File(path);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + reportName);
			response.setContentLength((int)file.length());
			
			FileInputStream stream = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			int bytes;
			while((bytes = stream.read()) != -1) {
				out.write(bytes);
			}
			
			stream.close();
			file.delete();
		}catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "DownloadReporteServlet : 59");
			}
			Log.getInstance().register(e, "DownloadReporteServlet : 61");
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
