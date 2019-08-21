package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class PartidosUsuarioLigaServlet
 */
@WebServlet("/partidosusuarioliga")
public class PartidosUsuarioLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
	int idLiga;
	int idUsuario;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PartidosUsuarioLigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String sDispute = request.getParameter("dispute");
		if(sDispute != null && !sDispute.isEmpty()) {
			try {
				negocio.Partido partido = datos.Partido.getInstance()
						.getOne(Integer.parseInt(sDispute));
				Calendar c = Calendar.getInstance();
				c.setTime(partido.getFecha());
				c.add(Calendar.DATE, negocio.Partido.MAX_DIAS_DISPUTA);
				Date today = new Date();
				if(today.after(c.getTime())) {
					request.setAttribute("err_dispute", "Han pasado mas de " + 
							negocio.Partido.MAX_DIAS_DISPUTA + " desde la fecha del partido");
				} else {
					datos.Disputa.getInstance().insert(partido.getId());
					datos.Partido.getInstance().disputarPartido(partido.getId());
					request.setAttribute("dispute_success", "El partido ha sido disputado "
							+ "correctamente");
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		String sEdit = request.getParameter("edit");
		if(sEdit != null && !sEdit.isEmpty()) {
			try {
				negocio.Partido partido = datos.Partido.getInstance().getOne(Integer.parseInt(sEdit));
				Calendar c = Calendar.getInstance();
				c.setTime(partido.getFecha());
				c.add(Calendar.DATE, negocio.Partido.MAX_DIAS_EDICION);
				Date today = new Date();
				if(today.after(c.getTime())) {
					request.setAttribute("err_edit", "Han pasado mas de " 
							+ negocio.Partido.MAX_DIAS_EDICION + " dias desde la fecha del "
							+ "partido");
				} else {
					request.setAttribute("id", sEdit);
					request.setAttribute("mode", "edit");
					request.setAttribute("coming_from", "partidosusuarioliga?skip=" + skip + "&search="
							+ lastSearch);
					request.getRequestDispatcher("registerMatchResult").forward(request, response);
					return;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		String sLiga = request.getParameter("idliga");
		if(sLiga != null && !sLiga.isEmpty()) {
			idLiga = Integer.parseInt(sLiga);
		}
		
		String sUsuario = request.getParameter("idusuario");
		if(sUsuario != null && !sUsuario.isEmpty()) {
			idUsuario = Integer.parseInt(sUsuario);
		}
		
		String toSearch = request.getParameter("search");
		if(toSearch != null && !toSearch.contentEquals(lastSearch)) {
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		request.setAttribute("idLiga", idLiga);
		request.setAttribute("idUsuario", idUsuario);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
		
		try {
			negocio.Liga liga = datos.Liga.getInstance().getOne(idLiga);
			negocio.Usuario usuario = datos.Usuario.getInstance().getOne(idUsuario);
			request.setAttribute("nombre_usuario", usuario.getNombre() + " (" + usuario.getApodo() + ")");
			request.setAttribute("nombre_liga", liga.getNombre() + " (" + liga.getTemporada() + ")");
			if(toSearch == null || toSearch.isEmpty()) {
				ArrayList<negocio.Partido> partidos = datos.Partido.getInstance()
						.getPartidosUsuarioLiga(idUsuario, idLiga, skip, LIMIT);
				
				count = datos.Partido.getInstance().getCountPartidosUsuarioLiga(idUsuario, idLiga);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidos", partidos);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} else {
				ArrayList<negocio.Partido> partidos = datos.Partido.getInstance()
						.getPartidosUsuarioLiga(idUsuario, idLiga, skip, LIMIT, toSearch);
				count = datos.Partido.getInstance().getCountPartidosUsuarioLiga(idUsuario, idLiga, 
						toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) ++maxPages;
				int currentPage = skip / LIMIT;
				
				request.setAttribute("partidos", partidos);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		request.getRequestDispatcher("partidosUsuarioLiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
