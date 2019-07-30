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
 * Servlet implementation class StatsLigaServlet
 */
@WebServlet("/estadisticasLiga")
public class StatsLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<negocio.UsuarioEstadisticas> estadisticasUsuarios = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatsLigaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int idLiga = Integer.parseInt(request.getParameter("id"));
			estadisticasUsuarios = datos.Liga.getInstance().getAllStatsUsuarios(idLiga);
			request.setAttribute("estadisticasUsuarios", estadisticasUsuarios);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("estadisticasLiga.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
