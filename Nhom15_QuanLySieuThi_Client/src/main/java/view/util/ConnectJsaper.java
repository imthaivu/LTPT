package view.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectJsaper {
	private Connection conn;

	public ConnectJsaper() {
		//TODO Auto-generated constructor stub
		connection();
	}
	

    //Kết nối đến csdl
    public Connection connection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLySieuThi;user=sa;password=sapassword");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return conn;
    }
}
