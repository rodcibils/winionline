package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	final protected static String[] imageExts = {".png", ".jpg", ".jpeg"};
	
	public static String getFileExtension(String filename)
	{
		if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0)
		{
			return filename.substring(filename.lastIndexOf(".") + 1);
		}
		
		return "";
	}
	
	private static String bytesToHex(byte[] bytes) 
	{
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
		v = bytes[j] & 0xFF;
		hexChars[j * 2] = hexArray[v >>> 4];
		hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		
		return new String(hexChars);
	}
	
	public static String encrypt(String key, String in)
	{
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(key.getBytes());
			md.update(in.getBytes());
			byte[] out = md.digest();
				
			return bytesToHex(out);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		
		return "";
	}
	
	public static boolean isImageFile(String fileName)
	{
		for(String ext : imageExts) {
			if(fileName.contains(ext)) return true;
		}
		
		return false;
	}
}
