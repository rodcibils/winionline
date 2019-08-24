package controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Log;

/**
 * Servlet implementation class NuevaLigaServlet
 */
@WebServlet("/newLeague")
public class NuevaLigaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NuevaLigaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		request.setAttribute("cur_season", currentYear);
		request.setAttribute("has_datepicker", "true");
		String mode = request.getParameter("mode");
		try {
			switch (mode)
			{
				case "insert":
					agregar(request, response);
					break;
				case "update":
					editar(request, response);
					break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("newLeague.jsp").forward(request, response);
	}
	
	private void agregar(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("old_mode", "insert");
		request.setAttribute("old_id", id);
		request.setAttribute("old_estado", id);
	}

	private void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		int id = Integer.parseInt(request.getParameter("id"));
		int estado = Integer.parseInt(request.getParameter("estado"));
		negocio.Liga nliga;
		datos.Liga liga = new datos.Liga();
		nliga = liga.getOne(id);
		request.setAttribute("old_mode", "update");
		request.setAttribute("old_estado", estado);
		request.setAttribute("old_id", id);
		request.setAttribute("old_nombre", nliga.getNombre());
		request.setAttribute("cur_season", nliga.getTemporada());
		request.setAttribute("old_sday", dateFormat.format(nliga.getInicio()));
		request.setAttribute("old_eday", dateFormat.format(nliga.getFin()));
		request.setAttribute("has_datepicker", "true");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mode = request.getParameter("mode");
		String sid = request.getParameter("id");
		String sestado = request.getParameter("estado");
		String name = request.getParameter("name");
		String season = request.getParameter("season");
		String startDay = request.getParameter("start-day");
		String endDay = request.getParameter("end-day");
		
		Date start = null, end = null;
		
		boolean isValid = true;
		
		if(name == null || name.isEmpty()) {
			request.setAttribute("err_nombre", "Campo obligatorio");
			isValid = false;
		}
		
		if(startDay == null || startDay.isEmpty()) {
			request.setAttribute("err_sday", "Campo obligatorio");
			isValid = false;
		} else {
			try {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				start = df.parse(startDay);
				request.setAttribute("old_sday", startDay);
			} catch(ParseException e) {
				request.setAttribute("err_sday", "La fecha debe tener formato dd/MM/yyyy");
				isValid = false;
			}
		}
		
		if(endDay == null || endDay.isEmpty()) {
			request.setAttribute("err_eday", "Campo obligatorio");
			isValid = false;
		} else {
			try {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				end = df.parse(endDay);
				request.setAttribute("old_eday", endDay);
			} catch(ParseException e) {
				request.setAttribute("err_eday", "La fecha debe tener formato dd/MM/yyyy");
				isValid = false;
			}
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 10);
		if(start != null && start.before(calendar.getTime())) {
			request.setAttribute("err_sday", "Deben faltar al menos 10 días hasta la fecha de inicio de la liga para que los usuarios puedan inscribirse");
			isValid = false;
		}
		
		if(start != null && end != null) {
			float days = (end.getTime() - start.getTime()) / (1000.0f*60.0f*60.0f*24.0f);
			if(days > 60.0f) {
				request.setAttribute("err_eday", "La liga no puede durar más de 60 días");
				isValid = false;
			}
			
			if(end.before(start)) {
				request.setAttribute("err_eday", "La fecha de fin de la liga no puede ser anterior a la fecha de inicio");
				isValid = false;
			}
		}
		
		if(!isValid) {
			request.setAttribute("old_mode", mode);
			request.setAttribute("old_id", sid);
			request.setAttribute("old_estado", sestado);
			request.setAttribute("old_nombre", name);
			request.setAttribute("cur_season", season);
			request.setAttribute("old_sday", startDay);
			request.setAttribute("old_eday", endDay);
			request.setAttribute("has_datepicker", "true");
			request.getRequestDispatcher("newLeague.jsp").forward(request, response);
		} else {
			negocio.Liga liga = new negocio.Liga();
			liga.setNombre(name);
			liga.setTemporada(Integer.parseInt(season));
			liga.setInicio(start);
			liga.setFin(end);
			datos.Liga dLiga = datos.Liga.getInstance();
			try 
			{
				switch(mode)
				{
					case "update":
						int id = Integer.parseInt(sid);
						liga.setId(id);
						dLiga.update(liga);		
						break;
					case "insert":
						if(!dLiga.checkIfLigaExists(liga))
						{
							dLiga.insert(liga);
						}
						else 
						{
							request.setAttribute("old_mode", mode);
							request.setAttribute("old_id", sid);
							request.setAttribute("old_id", sestado);
							request.setAttribute("err_nombre", "Ya existe una liga con ese nombre en esta temporada");
							request.setAttribute("old_nombre", name);
							request.setAttribute("cur_season", season);
							request.setAttribute("old_sday", startDay);
							request.setAttribute("old_eday", endDay);
							request.setAttribute("has_datepicker", "true");
							request.getRequestDispatcher("newLeague.jsp").forward(request, response);
						}
						break;
				}
				
				response.sendRedirect("wwligas?new_league_success=true");
			} catch(Exception e) {
				Log.getInstance().register(e, "NuevaLigaServlet : 203");
			}
		}
	}

}
