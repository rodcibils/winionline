package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Reportes;

/**
 * Servlet implementation class ReporteConflictoServlet
 */
@WebServlet("/reporteConflicto")
public class ReporteConflictoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReporteConflictoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String item = request.getParameter("item");
		if(item.contentEquals("disputas")) {
			try {
				ArrayList<negocio.Disputa> disputas = datos.Disputa.getInstance()
						.getDisputasCerradas(usuario.getId());
				
				for(negocio.Disputa disputa : disputas) {
					disputa.setVotosIndividualizados(datos.Disputa.getInstance()
							.getVotosIndividualizados(disputa.getPartido().getId()));
				}
				
				Reportes reporte = new Reportes();
				String filename = reporte.generarReporteDisputas(usuario.getNombre() + "-" + usuario.getApodo(), disputas, 
						usuario.getId());
				request.setAttribute("name", filename);
				request.getRequestDispatcher("downloadReporte").forward(request, response);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		} else if(item.contentEquals("apelaciones")) {
			
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
