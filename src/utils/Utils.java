package utils;

public class Utils {
	public static String getFileExtension(String filename)
	{
		if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0)
		{
			return filename.substring(filename.lastIndexOf(".") + 1);
		}
		
		return "";
	}
}
