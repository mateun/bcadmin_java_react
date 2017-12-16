package ttech.com.bitcoinapp.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionProvider 
{
	
	
	private static boolean loaded = false;
	
	

	public static Connection dbConnection()
	{
		
			try {
				if (!loaded)
				{
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				}
					
				return DriverManager.getConnection(System.getenv("db_conn_string"));
				
			} catch (ClassNotFoundException | SQLException se) {
				se.printStackTrace();
				return null;
			} 
		
		
	}
}
