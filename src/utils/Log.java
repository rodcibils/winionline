package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	private static Log instance = null;
	
	public static Log getInstance() {
		if(instance == null) instance = new Log();
		
		return instance;
	}
	
	public void register(Exception e, String originData)
	{
		try {
			String path = datos.Parametro.getInstance().getLogPath();
			path += "/log.txt";
			try(FileWriter writer = new FileWriter(path, true);
				BufferedWriter bw = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(bw))
			{
				Calendar c = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String line = "";
				if(e.getMessage() != null) {
					line = format.format(c.getTime()) + " | " + e.getMessage() + 
						"|" + originData;
				} else {
					line = format.format(c.getTime()) + " | " + originData;
				}
				out.println(line);
				
				out.close();
				bw.close();
				writer.close();
			} catch(IOException ex) {
				System.out.println(ex.getMessage());
			}
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
