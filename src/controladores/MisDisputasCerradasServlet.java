package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Log;

/**
 * Servlet implementation class MisDisputasCerradasServlet
 */
@WebServlet("/misDisputasCerradas")
public class MisDisputasCerradasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MisDisputasCerradasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String sApelacion = request.getParameter("apelar");
		if(sApelacion != null && !sApelacion.isEmpty()) {
			try {
				int apelacion = Integer.parseInt(sApelacion);
				datos.Disputa.getInstance().apelar(apelacion);
				datos.Apelacion.getInstance().create(apelacion);
				ArrayList<negocio.Usuario> jueces = datos.Usuario.getInstance()
						.getJuecesApelacion(apelacion);
				datos.Apelacion.getInstance().asignarJueces(apelacion, jueces);
				request.setAttribute("apelar_success", "Disputa apelada con exito");
				if((count - 1) % LIMIT == 0 && skip != 0) skip -= LIMIT;
			} catch(Exception e) {
				Log.getInstance().register(e, "MisDisputasCerradasServlet : 52");
			}
		}
		
		String toSearch = request.getParameter("search");
		if(toSearch != null && !toSearch.contentEquals(lastSearch)) {
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
		
		try {
			if(toSearch == null || toSearch.isEmpty()) {
				ArrayList<negocio.Disputa> disputas = datos.Disputa.getInstance()
						.getDisputasCerradas(usuario.getId(), skip, LIMIT);
				count = datos.Disputa.getInstance().getCountDisputasCerradas(usuario.getId());
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				for(negocio.Disputa disputa : disputas) {
					int votosUno = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(), 
							disputa.getPartido().getSolicitud().getJugadorUno().getId());
					int votosDos = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(), 
							disputa.getPartido().getSolicitud().getJugadorDos().getId());
					disputa.setVotosUno(votosUno);
					disputa.setVotosDos(votosDos);
					Calendar c = Calendar.getInstance();
					c.setTime(disputa.getVencimiento());
					c.add(Calendar.DATE, negocio.Disputa.MAX_DIAS_APELACION);
					Calendar today = Calendar.getInstance();
					disputa.setApelable(today.before(c));
				}
				
				request.setAttribute("disputas", disputas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Disputa> disputas = datos.Disputa.getInstance()
						.getDisputasCerradas(usuario.getId(), skip, LIMIT, toSearch);
				count = datos.Disputa.getInstance().getCountDisputasCerradas(usuario.getId(), toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				for(negocio.Disputa disputa : disputas) {
					int votosUno = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(), 
							disputa.getPartido().getSolicitud().getJugadorUno().getId());
					int votosDos = datos.Disputa.getInstance().getVotos(disputa.getPartido().getId(), 
							disputa.getPartido().getSolicitud().getJugadorDos().getId());
					disputa.setVotosUno(votosUno);
					disputa.setVotosDos(votosDos);
					Calendar c = Calendar.getInstance();
					c.setTime(disputa.getVencimiento());
					c.add(Calendar.DATE, negocio.Disputa.MAX_DIAS_APELACION);
					Calendar today = Calendar.getInstance();
					disputa.setApelable(today.before(c));
				}
				
				request.setAttribute("disputas", disputas);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher("listMisDisputasCerradas.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
