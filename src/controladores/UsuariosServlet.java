package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UsuariosServlet
 */
@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 10;
	private ArrayList<negocio.Usuario> usuarios = null;
	private int skip = 0;
	private int count = 0;
	private String lastSearch = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsuariosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		negocio.Usuario usuarioActual = (negocio.Usuario)request.getSession().getAttribute("usuario");
		
		String idRival = request.getParameter("desafiar");
		if(idRival != null && !idRival.isEmpty()) {	
			try {
				if(!datos.Solicitud.getInstance().checkIfExists(usuarioActual.getId(), 
						Integer.parseInt(idRival))) {
					datos.Solicitud.getInstance().createSolicitudAmistoso(usuarioActual.getId(),
							Integer.parseInt(idRival));
					request.setAttribute("challenge_success", true);
				} else {
					request.setAttribute("challenge_success", false);
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {				
			usuarios = datos.Usuario.getInstance().getAll(usuarioActual);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		request.setAttribute("usuarios", usuarios);
		String toSearch = request.getParameter("search");
		
		if(toSearch!=null && !toSearch.contentEquals(lastSearch)){
			lastSearch = toSearch;
			skip = 0;
		}
		request.setAttribute("search", lastSearch);
		
		String sSkip = request.getParameter("skip");
		if(sSkip != null) {
			skip = Integer.parseInt(sSkip);
		}
	
		try {
			if(toSearch == null || toSearch.contentEquals("")) 
			{
				count = datos.Usuario.getInstance()
						.getAllCount(usuarioActual);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Usuario> usuarios = datos.Usuario.getInstance()
						.getUsuariosPagination(skip, LIMIT, usuarioActual);
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else 
			{
				count = datos.Usuario.getInstance()
						.getCountUsuariosFiltered(toSearch, usuarioActual);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Usuario> usuarios = datos.Usuario.getInstance()
						.getUsuariosPagination(skip, LIMIT, toSearch, usuarioActual);
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		request.getRequestDispatcher("listUsuarios.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
