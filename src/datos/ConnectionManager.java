package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager
{
	private static ConnectionManager instance = null;
	private Connection conn = null;
	private int conn_count = 0;
	
	public static ConnectionManager getInstance()
	{
		if(instance == null) instance = new ConnectionManager();
		
		return instance;
	}
	
	public Connection getConnection() throws SQLException
	{
		if(conn == null)
		{
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/winionline", "root", 
							"root");
		}
		++conn_count;
		return conn;
	}
	
	public void closeConnection() throws SQLException
	{
		--conn_count;
		if(conn_count==0) {
			conn.close();
			conn = null;
		}
	}
}
