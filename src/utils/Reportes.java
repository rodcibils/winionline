package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import negocio.UsuarioEstadisticas;

public class Reportes 
{		
		public String generarReporteLiga(negocio.Liga liga, 
				ArrayList<negocio.UsuarioEstadisticas> estadisticas,
				ArrayList<negocio.Partido> partidos, String userName) 
						throws ClassNotFoundException, SQLException, 
						DocumentException, MalformedURLException, IOException
		{
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Document document = new Document();
			String fileName = userName + "-" + format.format(c.getTime()) + ".pdf";
			fileName = fileName.replace("/", "-");
			String serverPath = datos.Parametro.getInstance().getReportesPath();
			String path = serverPath + "/" + fileName;
			PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			
			String imgPath = serverPath + "/banner.jpg";
			Image banner = Image.getInstance(imgPath);
			banner.scalePercent(45);
			banner.setAlignment(Element.ALIGN_CENTER);
			document.add(banner);
			
			Font titleFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			
			Paragraph title = new Paragraph("Reporte de resultados de liga " + liga.getNombre() 
				+ " (temporada " + liga.getTemporada() + ")", titleFont);
			title.setSpacingBefore(20f);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			Font subtitleFont = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
			
			Paragraph data = new Paragraph("al " + dataFormat.format(c.getTime()) + " (Fin liga: "
					+ format.format(liga.getFin()) + ")");
			data.setSpacingAfter(20f);
			data.setAlignment(Element.ALIGN_CENTER);
			document.add(data);
			
			Paragraph titleTable = new Paragraph("Tabla de posiciones", subtitleFont);
			titleTable.setSpacingAfter(20f);
			titleTable.setAlignment(Element.ALIGN_CENTER);
			document.add(titleTable);
			
			PdfPTable posTable = new PdfPTable(10);
			Stream.of("º", "Jugador", "Pts", "PJ", "PG", "PE", "PP", "GF", "GC", "DG")
			.forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setVerticalAlignment(Element.ALIGN_CENTER);
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(2);
				header.setPhrase(new Phrase(columnTitle));
				posTable.addCell(header);
			});
			
			posTable.setWidths(new int[] {10,30,10,10,10,10,10,10,10,10});
			posTable.setSpacingAfter(20f);
			
			for(int i=0; i<estadisticas.size(); ++i) {
				UsuarioEstadisticas estadistica = estadisticas.get(i);
				
				PdfPCell posCell = new PdfPCell(new Phrase(Integer.toString(i+1)));
				posCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				posCell.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posCell);
				
				PdfPCell posNom = new PdfPCell(new Phrase(estadistica.getNombre()));
				posNom.setHorizontalAlignment(Element.ALIGN_CENTER);
				posNom.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posNom);
				
				PdfPCell posPun = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getPuntos())));
				posPun.setHorizontalAlignment(Element.ALIGN_CENTER);
				posPun.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posPun);
				
				PdfPCell posPJ = new PdfPCell(new Phrase(Integer.toString(estadistica
						.getPartJugados())));
				posPJ.setHorizontalAlignment(Element.ALIGN_CENTER);
				posPJ.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posPJ);
				
				PdfPCell posPG = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getPartGanados())));
				posPG.setHorizontalAlignment(Element.ALIGN_CENTER);
				posPG.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posPG);
				
				PdfPCell posPE = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getPartEmpatados())));
				posPE.setHorizontalAlignment(Element.ALIGN_CENTER);
				posPE.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posPE);
				
				PdfPCell posPP = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getPartPerdidos())));
				posPP.setHorizontalAlignment(Element.ALIGN_CENTER);
				posPP.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posPP);
				
				PdfPCell posGF = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getGolesFavor())));
				posGF.setHorizontalAlignment(Element.ALIGN_CENTER);
				posGF.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posGF);
				
				PdfPCell posGC = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getGolesContra())));
				posGC.setHorizontalAlignment(Element.ALIGN_CENTER);
				posGC.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posGC);
				
				PdfPCell posGD = new PdfPCell(new Phrase(Integer
						.toString(estadistica.getGolesDiferencia())));
				posGD.setHorizontalAlignment(Element.ALIGN_CENTER);
				posGD.setVerticalAlignment(Element.ALIGN_CENTER);
				posTable.addCell(posGD);
			}
			
			document.add(posTable);
			
			Paragraph titleMatch = new Paragraph("Partidos", subtitleFont);
			titleMatch.setSpacingAfter(20f);
			titleMatch.setAlignment(Element.ALIGN_CENTER);
			document.add(titleMatch);
			
			PdfPTable matchTable = new PdfPTable(7);
			Stream.of("º", "Fecha", "Jugador Uno", "", "", "", "Jugador Dos")
			.forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setVerticalAlignment(Element.ALIGN_CENTER);
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(2);
				header.setPhrase(new Phrase(columnTitle));
				matchTable.addCell(header);
			});
			
			matchTable.setWidths(new int[] {10,30,30,10,10,10,30});
			
			for(int i=0; i<partidos.size(); ++i) {
				negocio.Partido partido = partidos.get(i);
				
				PdfPCell numCell = new PdfPCell(new Phrase(Integer.toString(i+1)));
				numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				numCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(numCell);
				
				PdfPCell dateCell = new PdfPCell(new Phrase(format.format(partido.getFecha())));
				dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dateCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(dateCell);
				
				PdfPCell juCell = new PdfPCell(new Phrase(partido.getResultadoUno()
						.getJugador().getNombre() + " (" + partido.getResultadoUno()
						.getJugador().getApodo() + ")"));
				juCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				juCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(juCell);
				
				PdfPCell ruCell = new PdfPCell(new Phrase(Integer.toString(partido
						.getResultadoUno()
						.getGoles())));
				ruCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ruCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(ruCell);
				
				PdfPCell vsCell = new PdfPCell(new Phrase("Vs."));
				vsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				vsCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(vsCell);
				
				PdfPCell rdCell = new PdfPCell(new Phrase(Integer.toString(partido
						.getResultadoDos()
						.getGoles())));
				rdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				rdCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(rdCell);
				
				PdfPCell jdCell = new PdfPCell(new Phrase(partido.getResultadoDos()
						.getJugador().getNombre() + " (" + partido.getResultadoDos()
						.getJugador().getApodo() + ")"));
				jdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jdCell.setVerticalAlignment(Element.ALIGN_CENTER);
				matchTable.addCell(jdCell);
			}
			
			document.add(matchTable);
			
			document.close();
			
			return fileName;
		}
		
		public String generarReporteUsuario(String username,
				ArrayList<negocio.Liga> ligas,
				ArrayList<negocio.UsuarioEstadisticas> resultadosLigas,
				ArrayList<negocio.Partido> partidosLiga,
				ArrayList<negocio.Partido> amistosos) throws ClassNotFoundException, 
				SQLException, DocumentException, MalformedURLException, IOException
		{
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dateWithHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat onlyDate = new SimpleDateFormat("dd/MM/yyyy");
			
			Document document = new Document();
			String fileName = username + "-" + onlyDate.format(c.getTime()) + ".pdf";
			fileName = fileName.replace("/", "-");
			String serverPath = datos.Parametro.getInstance().getReportesPath();
			String path = serverPath + "/" + fileName;
			PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			
			String imgPath = serverPath + "/banner.jpg";
			Image banner = Image.getInstance(imgPath);
			banner.scalePercent(45);
			banner.setAlignment(Element.ALIGN_CENTER);
			document.add(banner);
			
			Font titleFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Font subtitleFont = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
			
			Paragraph title = new Paragraph("Reporte de historial general del usuario " + 
					username, titleFont);
			title.setSpacingBefore(20f);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			Paragraph subtitle = new Paragraph("al " + dateWithHour.format(c.getTime()), 
					subtitleFont);
			subtitle.setSpacingAfter(20f);
			subtitle.setAlignment(Element.ALIGN_CENTER);
			document.add(subtitle);
			
			Paragraph leaguesTitle = new Paragraph("Participaciones en ligas", subtitleFont);
			leaguesTitle.setSpacingAfter(20f);
			leaguesTitle.setAlignment(Element.ALIGN_CENTER);
			document.add(leaguesTitle);
			
			for(int i=0; i<ligas.size(); ++i) {
				negocio.Liga liga = ligas.get(i);
				negocio.UsuarioEstadisticas stats = resultadosLigas.get(i);
				
				Paragraph nombreLiga = new Paragraph("Liga: " + liga.getNombre(), subtitleFont);
				Paragraph temporadaLiga = new Paragraph("Temporada: " + 
						Integer.toString(liga.getTemporada()), subtitleFont);
				Paragraph inicioLiga = new Paragraph("Fecha inicio: " + 
						onlyDate.format(liga.getInicio()), subtitleFont);
				Paragraph finLiga = new Paragraph("Fecha fin: " + onlyDate.format(liga.getFin()), 
						subtitleFont);
				Paragraph posicionLiga = new Paragraph("Posicion: " + 
						Integer.toString(stats.getPosicion()), subtitleFont);
				Paragraph puntosLiga = new Paragraph("Puntos: " + Integer.toString(stats.getPuntos()),
						subtitleFont);
				Paragraph partidos = new Paragraph(
						Integer.toString(stats.getPartJugados()) + " PJ (" + 
						Integer.toString(stats.getPartGanados()) + " PG, " +
						Integer.toString(stats.getPartEmpatados()) + " PE, " +
						Integer.toString(stats.getPartPerdidos()) + " PP)", subtitleFont);
				Paragraph golesLiga = new Paragraph(
						Integer.toString(stats.getGolesFavor()) + " GF, " +
						Integer.toString(stats.getGolesContra()) + " GC, " + 
						Integer.toString(stats.getGolesDiferencia()) + " DG", subtitleFont);
				
				golesLiga.setSpacingAfter(20f);
				
				document.add(nombreLiga);
				document.add(temporadaLiga);
				document.add(inicioLiga);
				document.add(finLiga);
				document.add(posicionLiga);
				document.add(puntosLiga);
				document.add(partidos);
				document.add(golesLiga);
				
				PdfPTable matchTable = new PdfPTable(6);
				Stream.of("Fecha", "Jugador Uno", "", "", "", "Jugador Dos")
				.forEach(columnTitle ->{
					PdfPCell header = new PdfPCell();
					header.setHorizontalAlignment(Element.ALIGN_CENTER);
					header.setVerticalAlignment(Element.ALIGN_CENTER);
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(2);
					header.setPhrase(new Phrase(columnTitle));
					matchTable.addCell(header);
				});
				matchTable.setSpacingAfter(30f);
				
				for(int j=0; j<partidosLiga.size(); ++j) {
					negocio.Partido partido = partidosLiga.get(j);
					if(partido.getSolicitud().getLiga().getId() == liga.getId()) {
						PdfPCell fechaCell = new PdfPCell(new Phrase(onlyDate.format(partido.getFecha())));
						fechaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						fechaCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(fechaCell);
						
						PdfPCell juCell = new PdfPCell(new Phrase(partido.getResultadoUno()
								.getJugador().getNombre() + " (" + partido.getResultadoUno()
								.getJugador().getApodo() + ")"));
						juCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						juCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(juCell);
						
						PdfPCell ruCell = new PdfPCell(new Phrase(Integer.toString(partido
								.getResultadoUno()
								.getGoles())));
						ruCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						ruCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(ruCell);
						
						PdfPCell vsCell = new PdfPCell(new Phrase("Vs."));
						vsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						vsCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(vsCell);
						
						PdfPCell rdCell = new PdfPCell(new Phrase(Integer.toString(partido
								.getResultadoDos()
								.getGoles())));
						rdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						rdCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(rdCell);
						
						PdfPCell jdCell = new PdfPCell(new Phrase(partido.getResultadoDos()
								.getJugador().getNombre() + " (" + partido.getResultadoDos()
								.getJugador().getApodo() + ")"));
						jdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						jdCell.setVerticalAlignment(Element.ALIGN_CENTER);
						matchTable.addCell(jdCell);
					}
				}
				
				document.add(matchTable);
			}
			
			Paragraph amistososTitle = new Paragraph("Partidos Amistosos", subtitleFont);
			amistososTitle.setSpacingBefore(20f);
			amistososTitle.setSpacingAfter(20f);
			amistososTitle.setAlignment(Element.ALIGN_CENTER);
			document.add(amistososTitle);
			
			PdfPTable amistososTable = new PdfPTable(6);
			Stream.of("Fecha", "Jugador Uno", "", "", "", "Jugador Dos")
			.forEach(columnTitle ->{
				PdfPCell header = new PdfPCell();
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setVerticalAlignment(Element.ALIGN_CENTER);
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(2);
				header.setPhrase(new Phrase(columnTitle));
				amistososTable.addCell(header);
			});
			amistososTable.setSpacingAfter(30f);
			
			for(int i=0; i<amistosos.size(); ++i) {
				negocio.Partido partido = amistosos.get(i);
				
				PdfPCell fechaCell = new PdfPCell(new Phrase(onlyDate.format(partido.getFecha())));
				fechaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				fechaCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(fechaCell);
				
				PdfPCell juCell = new PdfPCell(new Phrase(partido.getResultadoUno()
						.getJugador().getNombre() + " (" + partido.getResultadoUno()
						.getJugador().getApodo() + ")"));
				juCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				juCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(juCell);
				
				PdfPCell ruCell = new PdfPCell(new Phrase(Integer.toString(partido
						.getResultadoUno()
						.getGoles())));
				ruCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ruCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(ruCell);
				
				PdfPCell vsCell = new PdfPCell(new Phrase("Vs."));
				vsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				vsCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(vsCell);
				
				PdfPCell rdCell = new PdfPCell(new Phrase(Integer.toString(partido
						.getResultadoDos()
						.getGoles())));
				rdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				rdCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(rdCell);
				
				PdfPCell jdCell = new PdfPCell(new Phrase(partido.getResultadoDos()
						.getJugador().getNombre() + " (" + partido.getResultadoDos()
						.getJugador().getApodo() + ")"));
				jdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jdCell.setVerticalAlignment(Element.ALIGN_CENTER);
				amistososTable.addCell(jdCell);
			}
			
			document.add(amistososTable);
			
			document.close();
			
			return fileName;
		}
		
		public String generarReporteDisputas(String username, ArrayList<negocio.Disputa> disputas,
				int id) throws ClassNotFoundException, SQLException, 
				DocumentException, MalformedURLException, IOException
		{
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dateWithHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat onlyDate = new SimpleDateFormat("dd/MM/yyyy");
			
			Document document = new Document();
			String filename = username + "-" + onlyDate.format(c.getTime()) + ".pdf";
			filename = filename.replace("/", "-");
			String path = datos.Parametro.getInstance().getReportesPath();
			PdfWriter.getInstance(document, new FileOutputStream(path + "/" + filename));
			document.open();
			
			String imgPath = path + "/banner.jpg";
			Image banner = Image.getInstance(imgPath);
			banner.scalePercent(45);
			banner.setAlignment(Element.ALIGN_CENTER);
			document.add(banner);
			
			Font titleFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Font subtitleFont = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
			
			Paragraph title = new Paragraph("Reporte de disputas cerradas del usuario " + 
					username, titleFont);
			title.setSpacingBefore(20f);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			Paragraph subTitle = new Paragraph("al " + dateWithHour.format(c.getTime()), subtitleFont);
			subTitle.setSpacingAfter(20f);
			subTitle.setAlignment(Element.ALIGN_CENTER);
			document.add(subTitle);
			
			for(negocio.Disputa disputa : disputas) {
				Paragraph rivalP;
				Paragraph resultadoP;
				if(disputa.getPartido().getResultadoUno().getJugador().getId() == id) {
					rivalP = new Paragraph("Rival: " + disputa.getPartido().getResultadoDos()
							.getJugador().getNombre() + " (" + disputa.getPartido().getResultadoDos()
							.getJugador().getApodo() + ")", subtitleFont);
				} else {
					rivalP = new Paragraph("Rival: " + disputa.getPartido().getResultadoUno()
							.getJugador().getNombre() + " (" + disputa.getPartido().getResultadoUno()
							.getJugador().getApodo() + ")", subtitleFont);
				}
				
				Paragraph fechaPP = new Paragraph("Fecha partido: " + 
						onlyDate.format(disputa.getPartido().getFecha()), subtitleFont);
				Paragraph fechaDP = new Paragraph("Fecha disputa:" +
						onlyDate.format(disputa.getFecha()), subtitleFont);
				Paragraph fechaVDP = new Paragraph("Fecha vencimiento disputa: " + 
						onlyDate.format(disputa.getVencimiento()), subtitleFont);
				
				int votosPropios = 0;
				int votosRival = 0;
				
				PdfPTable votosTable = new PdfPTable(2);
				Stream.of("Usuario", "Voto").forEach(columnTitle ->{
					PdfPCell header = new PdfPCell();
					header.setHorizontalAlignment(Element.ALIGN_CENTER);
					header.setVerticalAlignment(Element.ALIGN_CENTER);
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(2);
					header.setPhrase(new Phrase(columnTitle));
					votosTable.addCell(header);
				});
				
				votosTable.setSpacingBefore(10f);
				votosTable.setSpacingAfter(30f);
				
				for(Map.Entry<negocio.Usuario, negocio.Usuario> entry : disputa.getVotosIndividualizados().entrySet())
				{
					negocio.Usuario votante = entry.getKey();
					negocio.Usuario votado = entry.getValue();
					
					PdfPCell votanteCell = new PdfPCell(new Phrase(votante.getNombre() + " (" + votante.getApodo() + ")"));
					votanteCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					votanteCell.setVerticalAlignment(Element.ALIGN_CENTER);
					votosTable.addCell(votanteCell);
					
					PdfPCell votadoCell = new PdfPCell(new Phrase(votado.getNombre() + " (" + votado.getApodo() + ")"));
					votadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					votadoCell.setVerticalAlignment(Element.ALIGN_CENTER);
					votosTable.addCell(votadoCell);
					
					if(votado.getId() == id) ++votosPropios;
					else ++votosRival;
				}
				
				if(votosPropios > votosRival) {
					resultadoP = new Paragraph(new Phrase("Resultado: " 
							+ Integer.toString(votosPropios) + " - " + 
							Integer.toString(votosRival) + " (V)", subtitleFont));
				} else if(votosPropios < votosRival){
					resultadoP = new Paragraph(new Phrase("Resultado: " 
							+ Integer.toString(votosPropios) + " - " + 
							Integer.toString(votosRival) + " (D)", subtitleFont));
				} else {
					resultadoP = new Paragraph(new Phrase("Resultado: " 
							+ Integer.toString(votosPropios) + " - " + 
							Integer.toString(votosRival) + " (E)", subtitleFont));
				}
				
				document.add(rivalP);
				document.add(resultadoP);
				document.add(fechaPP);
				document.add(fechaDP);
				document.add(fechaVDP);
				document.add(votosTable);
			}
			
			document.close();
			
			return filename;
		}
		
		public String generarReporteApelaciones(String username, 
				ArrayList<negocio.Apelacion> apelaciones, int id) throws DocumentException, 
				ClassNotFoundException, SQLException, MalformedURLException, IOException
		{
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dateWithHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat onlyDate = new SimpleDateFormat("dd/MM/yyyy");
			
			Document document = new Document();
			String filename = username + "-" + onlyDate.format(c.getTime()) + ".pdf";
			filename = filename.replace("/", "-");
			String path = datos.Parametro.getInstance().getReportesPath();
			PdfWriter.getInstance(document, new FileOutputStream(path + "/" + filename));
			document.open();
			
			String imgPath = path + "/banner.jpg";
			Image banner = Image.getInstance(imgPath);
			banner.scalePercent(45);
			banner.setAlignment(Element.ALIGN_CENTER);
			document.add(banner);
			
			Font titleFont = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Font subtitleFont = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
			
			Paragraph title = new Paragraph("Reporte de apelaciones cerradas del usuario " + 
					username, titleFont);
			title.setSpacingBefore(20f);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			Paragraph subTitle = new Paragraph("al " + dateWithHour.format(c.getTime()), subtitleFont);
			subTitle.setSpacingAfter(20f);
			subTitle.setAlignment(Element.ALIGN_CENTER);
			document.add(subTitle);
			
			for(negocio.Apelacion apelacion : apelaciones) {
				Paragraph rivalP;
				Paragraph resultadoP;
				if(apelacion.getDisputa().getPartido().getSolicitud().getJugadorUno().getId() == id) {
					rivalP = new Paragraph("Rival: " + apelacion.getDisputa().getPartido().getSolicitud()
							.getJugadorDos().getNombre() + " (" + apelacion.getDisputa().getPartido().getSolicitud()
							.getJugadorDos().getApodo() + ")", subtitleFont);
					
				} else {
					rivalP = new Paragraph("Rival: " + apelacion.getDisputa().getPartido().getSolicitud()
							.getJugadorUno().getNombre() + " (" + apelacion.getDisputa().getPartido()
							.getSolicitud().getJugadorUno().getApodo() + ")", subtitleFont);
				}
				
				Paragraph fechaPP = new Paragraph("Fecha partido: " + 
						onlyDate.format(apelacion.getDisputa().getPartido().getFecha()), subtitleFont);
				Paragraph fechaDP = new Paragraph("Fecha apelacion:" +
						onlyDate.format(apelacion.getFecha()), subtitleFont);
				Paragraph fechaVDP = new Paragraph("Fecha cierre apelacion: " + 
						onlyDate.format(apelacion.getCierre()), subtitleFont);
				
				int votosPropios = 0;
				int votosRival = 0;
				
				PdfPTable votosTable = new PdfPTable(2);
				Stream.of("Usuario", "Voto").forEach(columnTitle ->{
					PdfPCell header = new PdfPCell();
					header.setHorizontalAlignment(Element.ALIGN_CENTER);
					header.setVerticalAlignment(Element.ALIGN_CENTER);
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(2);
					header.setPhrase(new Phrase(columnTitle));
					votosTable.addCell(header);
				});
				
				votosTable.setSpacingBefore(10f);
				votosTable.setSpacingAfter(30f);
				
				for(Map.Entry<negocio.Usuario, negocio.Usuario> entry : apelacion.getVotosIndividualizados().entrySet())
				{
					negocio.Usuario votante = entry.getKey();
					negocio.Usuario votado = entry.getValue();
					
					PdfPCell votanteCell = new PdfPCell(new Phrase(votante.getNombre() + " (" + votante.getApodo() + ")"));
					votanteCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					votanteCell.setVerticalAlignment(Element.ALIGN_CENTER);
					votosTable.addCell(votanteCell);
					
					PdfPCell votadoCell = new PdfPCell(new Phrase(votado.getNombre() + " (" + votado.getApodo() + ")"));
					votadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					votadoCell.setVerticalAlignment(Element.ALIGN_CENTER);
					votosTable.addCell(votadoCell);
					
					if(votado.getId() == id) ++votosPropios;
					else ++votosRival;
				}
				
				if(votosPropios > votosRival) {
					resultadoP = new Paragraph(new Phrase("Resultado: " + Integer.toString(votosPropios) + 
							" - " + Integer.toString(votosRival) + " (V)", subtitleFont));
				} else {
					resultadoP = new Paragraph(new Phrase("Resultado: " + Integer.toString(votosPropios) + 
							" - " + Integer.toString(votosRival) + " (D)", subtitleFont));
				}
				
				document.add(rivalP);
				document.add(resultadoP);
				document.add(fechaPP);
				document.add(fechaDP);
				document.add(fechaVDP);
				document.add(votosTable);
			}
			
			document.close();
			
			return filename;
		}
}
