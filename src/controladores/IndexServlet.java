package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		request.setAttribute("data_getted", true);
		try {	
			int cantSolAmRecPend = datos.Solicitud.getInstance()
					.getCountSolicitudesRecibidasAmistososPendientes(usuario);
			request.setAttribute("sol_am_rec_pend", cantSolAmRecPend);
			
			int cantSolAmEnvPend = datos.Solicitud.getInstance()
					.getCountSolicitudesEnviadasAmistososPendientes(usuario);
			request.setAttribute("sol_am_env_pend", cantSolAmEnvPend);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
