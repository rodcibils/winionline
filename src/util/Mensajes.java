package util;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Mensajes extends ArrayList<String>{
	public boolean add(String mensaje, String type) {
        if(type.equals("error")) {
            return super.add("<div class=\"yellow-text\"><i class=\"material-icons tiny\">&#xE002;</i> Error<br>" + mensaje + "</div>");
        }
        else if(type.equals("success")) {
            return super.add(mensaje);
        }
        
        return false;
    }
}
