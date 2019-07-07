package controladores;

import java.io.IOException;
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
		request.getRequestDispatcher("newLeague.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			try {
				if(!dLiga.checkIfLigaExists(liga)) {
					dLiga.insert(liga);
					response.sendRedirect("index.jsp?new_league_success=true");
				} else {
					request.setAttribute("err_nombre", "Ya existe una liga con ese nombre en esta temporada");
					request.setAttribute("old_nombre", name);
					request.setAttribute("cur_season", season);
					request.setAttribute("old_sday", startDay);
					request.setAttribute("old_eday", endDay);
					request.setAttribute("has_datepicker", "true");
					request.getRequestDispatcher("newLeague.jsp").forward(request, response);
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
