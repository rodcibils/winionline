package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class Reportes {
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
}
