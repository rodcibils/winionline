package controladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.Evidencia;
import utils.Utils;

/**
 * Servlet implementation class EvidenciaServlet
 */
@WebServlet("/evidencia")
public class EvidenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EvidenciaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idDisputa = request.getParameter("id");
		String idJugador = request.getParameter("jugador");
		
		String votar = request.getParameter("vote");
		if(votar != null && !votar.isEmpty()) {
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			try {
				if(datos.Disputa.getInstance().checkApelacion(Integer.parseInt(votar))) {
					datos.Apelacion.getInstance().votarApelacion(usuario.getId(),
							Integer.parseInt(votar), Integer.parseInt(idJugador));
					request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
					request.getRequestDispatcher("apelacionesAJuzgar?skip=0&search=")
						.forward(request, response);
				} else {
					datos.Disputa.getInstance().votarDisputa(usuario.getId(), 
							Integer.parseInt(votar), Integer.parseInt(idJugador));
					request.setAttribute("vote_success", "El voto ha sido registrado exitosamente");
					request.getRequestDispatcher("listDisputas?skip=0&search=").forward(request, response);
					return;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		ArrayList<Evidencia> evidencias = new ArrayList<>();
		try {
			negocio.Disputa disputa = datos.Disputa.getInstance().getOne(Integer.parseInt(idDisputa));
			negocio.Usuario jugador = datos.Usuario.getInstance().getOne(Integer.parseInt(idJugador));
			String path = datos.Parametro.getInstance().getEvidenciasPath();
			File folder = new File(path + "/" + idDisputa + "/" + idJugador);
			if(folder.exists()) {
				for(File file : folder.listFiles()) {
					Evidencia evidencia = new Evidencia();
					evidencia.setFecha(new Date(file.lastModified()));
					evidencia.setPath("/" + idDisputa + "/" + idJugador + "/" + file.getName());
					if(Utils.isImageFile(file.getPath())) {
						evidencia.setTipo(Evidencia.IMAGEN);
					} else {
						evidencia.setTipo(Evidencia.VIDEO);
						try {
							FileReader reader = new FileReader(file.getPath());
							BufferedReader buffer = new BufferedReader(reader);
							String line;
							while((line = buffer.readLine()) != null) {
								String embedLine = line.split("&")[0].replace("watch?v=", "embed/");
								evidencia.setLink(embedLine);
							}
							buffer.close();
						}catch(Exception e) {
							System.out.println(e.getMessage());
						}
					}
					evidencias.add(evidencia);
				}
			}
				
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			request.setAttribute("jugador", jugador.getNombre() + " - " + jugador.getApodo());
			request.setAttribute("fecha_partido", sdf.format(disputa.getPartido().getFecha()));
			request.setAttribute("vencimiento", sdf.format(disputa.getVencimiento()));
			request.setAttribute("resultado_partido" , disputa.getPartido().getResultadoUno()
					.getGoles() + " - " + disputa.getPartido().getResultadoDos().getGoles());
			if(disputa.getPartido().getResultadoUno().getJugador().getId() 
					== Integer.parseInt(idJugador))
			{
				request.setAttribute("rival", disputa.getPartido().getResultadoUno()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoUno()
						.getJugador().getApodo());
			} else {
				request.setAttribute("rival", disputa.getPartido().getResultadoDos()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoDos()
						.getJugador().getApodo());
			}
			request.setAttribute("evidencias", evidencias);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		request.setAttribute("id_disputa", idDisputa);
		request.setAttribute("id_jugador", idJugador);
		request.setAttribute("count", evidencias.size());
		request.getRequestDispatcher("verEvidenciasDisputa.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
