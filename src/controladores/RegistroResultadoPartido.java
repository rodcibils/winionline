package controladores;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistroResultadoPartido
 */
@WebServlet("/registerMatchResult")
public class RegistroResultadoPartido extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private negocio.Partido partido = null;
	private String comingFrom = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistroResultadoPartido() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idPartido = (String)request.getAttribute("id_partido");
		comingFrom = (String)request.getAttribute("coming_from");
		
		try {
			partido = datos.Partido.getInstance().getOne(Integer.parseInt(idPartido));
			request.setAttribute("partido", partido);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("registerMatchResult.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sGolesUno = request.getParameter("golesUno");
		String sGolesDos = request.getParameter("golesDos");
		
		boolean isValid = true;
		if(sGolesUno == null || sGolesUno.isEmpty()) {
			isValid = false;
			request.setAttribute("err_guno", "Campo obligatorio");
		}
		if(sGolesDos == null || sGolesDos.isEmpty()) {
			isValid = false;
			request.setAttribute("err_gdos", "Campo obligatorio");
		}
		
		if(isValid) {
			int golesUno = Integer.parseInt(sGolesUno);
			int golesDos = Integer.parseInt(sGolesDos);
			
			negocio.Resultado resultadoUno = new negocio.Resultado();
			resultadoUno.setJugador(partido.getSolicitud().getJugadorUno());
			resultadoUno.setPartido(partido);
			resultadoUno.setGoles(golesUno);
			
			negocio.Resultado resultadoDos = new negocio.Resultado();
			resultadoDos.setJugador(partido.getSolicitud().getJugadorDos());
			resultadoDos.setPartido(partido);
			resultadoDos.setGoles(golesDos);
			
			try {
				datos.Resultado.getInstance().insert(resultadoUno);
				datos.Resultado.getInstance().insert(resultadoDos);
				negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
				datos.Partido.getInstance().finalizarPartido(partido.getId(), usuario.getId());
				request.setAttribute("register_success", true);
				request.getRequestDispatcher(comingFrom).forward(request, response);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} else {
			request.setAttribute("partido", partido);
			request.setAttribute("coming_from", comingFrom);
			request.setAttribute("old_guno", sGolesUno);
			request.setAttribute("old_gdos", sGolesDos);
			request.getRequestDispatcher("registerMatchResult.jsp").forward(request, response);
		}
	}

}
