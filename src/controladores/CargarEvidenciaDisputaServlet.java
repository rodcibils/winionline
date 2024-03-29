package controladores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import datos.ConnectionManager;
import negocio.Evidencia;
import utils.Log;
import utils.Utils;

/**
 * Servlet implementation class CargarEvidenciaDisputaServlet
 */
@WebServlet("/cargarEvidenciaDisputa")
@MultipartConfig
public class CargarEvidenciaDisputaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String id = "";
	private int count = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CargarEvidenciaDisputaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String toDelete = request.getParameter("delete");
		
		String sId = request.getParameter("id");
		if(sId != null && !sId.isEmpty()) {
			id = sId;
		}
		
		negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
		try {
			negocio.Disputa disputa = datos.Disputa.getInstance().getOne(Integer.parseInt(id));
			String path = datos.Parametro.getInstance().getEvidenciasPath();
			
			if(toDelete != null && !toDelete.isEmpty()) {
				File file = new File(path + toDelete);
				if(file.exists()) file.delete();
			}
			
			File folder = new File(path + "/" + id + "/" + Integer.toString(usuario.getId()));
			ArrayList<Evidencia> evidencias = new ArrayList<>();
			if(!folder.exists()) folder.mkdirs();
			for(File file : folder.listFiles()) {
				Evidencia evidencia = new Evidencia();
				evidencia.setFecha(new Date(file.lastModified()));
				evidencia.setPath("/" + id + "/" + Integer.toString(usuario.getId()) 
				+ "/" + file.getName());
				if(Utils.isImageFile(file.getPath())) {
					evidencia.setTipo(Evidencia.IMAGEN);
				} else { 
					evidencia.setTipo(Evidencia.VIDEO);
					try {
						FileReader reader = new FileReader(file.getPath());
						BufferedReader buffer = new BufferedReader(reader);
						String line;
						while((line = buffer.readLine()) != null) {
							evidencia.setLink(line);
						}
						buffer.close();
					} catch(Exception e) {
						Log.getInstance().register(e, "CargarEvidenciaDisputaServlet : 90");
					}
				}
				evidencias.add(evidencia);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			request.setAttribute("fecha_partido", sdf.format(disputa.getPartido().getFecha()));
			if(disputa.getPartido().getResultadoUno().getJugador().getId() == usuario.getId())
			{
				request.setAttribute("rival_partido", disputa.getPartido().getResultadoDos()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoDos()
						.getJugador().getApodo());
				int golesUno = disputa.getPartido().getResultadoUno().getGoles();
				int golesDos = disputa.getPartido().getResultadoDos().getGoles();
				String resultado = Integer.toString(golesUno) + " - " + Integer.toString(golesDos);
				if(golesUno > golesDos) resultado += " (V)";
				else if(golesUno < golesDos) resultado += " (D)";
				else resultado += " (E)";
				request.setAttribute("resultado_partido", resultado);
			} else {
				request.setAttribute("rival_partido", disputa.getPartido().getResultadoUno()
						.getJugador().getNombre() + " - " + disputa.getPartido().getResultadoUno()
						.getJugador().getApodo());
				int golesUno = disputa.getPartido().getResultadoUno().getGoles();
				int golesDos = disputa.getPartido().getResultadoDos().getGoles();
				String resultado = Integer.toString(golesUno) + " - " + Integer.toString(golesDos);
				if(golesUno > golesDos) resultado += " (D)";
				else if(golesUno < golesDos) resultado += " (V)";
				else resultado += " (E)";
				request.setAttribute("resultado_partido", resultado);
			}
			request.setAttribute("count", evidencias.size());
			count = evidencias.size();
			request.setAttribute("evidencias", evidencias);
		} catch(Exception e) {
			try {
				ConnectionManager.getInstance().closeConnection();
			} catch (SQLException e1) {
				Log.getInstance().register(e, "CargarEvidenciaDisputaServlet : 131");
			}
			Log.getInstance().register(e, "CargarEvidenciaDisputaServlet : 133");
		}
		
		String err_video = request.getParameter("err_video");
		if(err_video != null && !err_video.isEmpty()) {
			request.setAttribute("err_video", err_video);
		}
		
		String err_imagen = request.getParameter("err_imagen");
		if(err_imagen != null && !err_imagen.isEmpty()) {
			request.setAttribute("err_imagen", err_imagen);
		}
		
		request.getRequestDispatcher("loadEvidencia.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(count < negocio.Evidencia.MAX_EVIDENCIAS) 
		{
			negocio.Usuario usuario = (negocio.Usuario)request.getSession().getAttribute("usuario");
			
			Part filePart = request.getPart("imagen");
			if(filePart != null) {
				String fileName = Paths.get(filePart.getSubmittedFileName()).toString();
				if(fileName != null && !fileName.isEmpty()) {
					if(Utils.isImageFile(fileName)) {
						try(InputStream stream = filePart.getInputStream()){
							String ext = Utils.getFileExtension(fileName);
							try {
								String path = datos.Parametro.getInstance().getEvidenciasPath();
								File file = new File(path + "/" + id + "/" + 
										Integer.toString(usuario.getId()),
										Integer.toString(count) + "." + ext);
								Files.copy(stream, file.toPath());
							} catch(Exception e) {
								ConnectionManager.getInstance().closeConnection();
								Log.getInstance().register(e, "CargarEvidenciaDisputaSerlvet : 173");
							}
						} catch(Exception e) {
							Log.getInstance().register(e, "CargarEvidenciaServlet : 175");
						}
					} else {
						request.setAttribute("err_imagen", "El formato de imagen cargada no es valido");
					}
				}
			}
			
			String video = request.getParameter("video");
			if(video != null && !video.isEmpty()) {
				if(video.contains("www.youtube.com")) {
					try {
						String path = datos.Parametro.getInstance().getEvidenciasPath();
						Path file = Paths.get(path + "/" + id + "/" + Integer.toString(usuario.getId()) 
								+ "/" + Integer.toString(count) + ".txt");
						List<String> content = Arrays.asList(video);
						Files.write(file, content, StandardCharsets.UTF_8);
					} catch(Exception e) {
						try {
							ConnectionManager.getInstance().closeConnection();
						} catch (SQLException e1) {
							Log.getInstance().register(e, "CargarEvidenciaDisputaServlet : 196");
						}
						Log.getInstance().register(e, "CargarEvidenciaDisputaServlet : 198");
					}
				} else {
					request.setAttribute("err_video", "Las unicas url de video aceptadas deben ser de Youtube");
				}
			}
		} else {
			request.setAttribute("err_gral", "No puede cargar mas de 10 evidencias a una disputa");
		}
		
		doGet(request, response);
	}

}
