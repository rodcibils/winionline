package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Utils;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			try {
				usuarios = datos.Usuario.getInstance().getAll();
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
			
			int usuarioADesafiar;
			String desafiar = request.getParameter("desafiar");
			if(desafiar != null) {
				usuarioADesafiar = Integer.parseInt(desafiar);
			}
					
		
		try {
			if(toSearch == null || toSearch.contentEquals("")) {
				count = datos.Usuario.getInstance()
						.getAllCount();
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Usuario> usuarios = datos.Usuario.getInstance()
						.getUsuariosPagination(skip, LIMIT);
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			} 
			else {
				count = datos.Usuario.getInstance()
						.getCountUsuariosFiltered(toSearch);
				int maxPages = count / LIMIT;
				if(count % LIMIT != 0) {
					++maxPages;
				}
				
				int currentPage = skip / LIMIT;
				
				ArrayList<negocio.Usuario> usuarios = datos.Usuario.getInstance()
						.getUsuariosPagination(skip, LIMIT, toSearch);
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("skip", skip);
				request.setAttribute("current_page", currentPage);
				request.setAttribute("max_pages", maxPages);
				request.setAttribute("count", count);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		request.getRequestDispatcher("usuarios.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
