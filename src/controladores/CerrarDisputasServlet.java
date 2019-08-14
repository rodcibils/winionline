package controladores;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CerrarDisputasServlet
 */
@WebServlet("/cerrarDisputas")
public class CerrarDisputasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CerrarDisputasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ArrayList<negocio.Disputa> disputasVencidas = datos.Disputa.getInstance()
					.getDisputasVencidas();
			
			for(negocio.Disputa disputa : disputasVencidas) {
				int votosUno = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(),
						disputa.getPartido().getSolicitud().getJugadorUno().getId());
				int votosDos = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(),
						disputa.getPartido().getSolicitud().getJugadorDos().getId());
				
				negocio.Resultado resultadoUno = new negocio.Resultado();
				resultadoUno.setJugador(disputa.getPartido().getSolicitud().getJugadorUno());
				resultadoUno.setPartido(disputa.getPartido());
				negocio.Resultado resultadoDos = new negocio.Resultado();
				resultadoDos.setJugador(disputa.getPartido().getSolicitud().getJugadorDos());
				resultadoDos.setPartido(disputa.getPartido());
				if(votosUno > votosDos) {
					resultadoUno.setGoles(3);
					resultadoDos.setGoles(0);
				} else if (votosUno < votosDos) {
					resultadoUno.setGoles(0);
					resultadoDos.setGoles(3);
				} else {
					resultadoUno.setGoles(0);
					resultadoDos.setGoles(0);
				}
				
				datos.Resultado.getInstance().update(resultadoUno);
				datos.Resultado.getInstance().update(resultadoDos);
				
				negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
				datos.Partido.getInstance().finalizarPartido(disputa.getPartido().getId(), 
						usuario.getId());
				
				datos.Disputa.getInstance().cerrarDisputa(disputa.getPartido().getId());
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		response.sendRedirect("index?disputas_success=true");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
