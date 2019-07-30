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
 * Servlet implementation class PartidosUsuarioLigaServlet
 */
@WebServlet("/partidosusuarioliga")
public class PartidosUsuarioLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.ResultadoPartido> partidosLigaUsuario = null;
       
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int idLiga = Integer.parseInt(request.getParameter("idliga"));
			int idUsuario = Integer.parseInt(request.getParameter("idusuario"));
			partidosLigaUsuario = datos.Liga.getInstance().getAllPartidosLigaUsuario(idLiga, idUsuario);
			request.setAttribute("partidosligausuario", partidosLigaUsuario);
			request.setAttribute("idLiga", idLiga);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
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
