package main.java.DB;
import java.sql.*;

import javax.swing.*;
public class GttDBConnection {
	Connection conn=null;
	public static Connection dbConnector()
	{
		try {	
			Class.forName("org.mariadb.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mariadb://192.168.90.101/gttdatabase","gttuser","gttpassword");
			
			//JOptionPane.showMessageDialog(null, "Connection Successful");
			return conn;
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}
