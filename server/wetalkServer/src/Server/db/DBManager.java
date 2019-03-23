package Server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Server.db.DBManager;

public class DBManager {
	private static final DBManager dbManager = new DBManager();
	private static Connection connection = null;
	private static final String url = "jdbc:mysql://localhost/wetalk?"
			+ "useUnicode=true&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC";
	private Statement statement;
	
	public static DBManager getDBManager() {
		return dbManager;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private DBManager() { }

	public void addDBDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Success loading Mysql Driver!");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {	
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
	}
	
	
	public void connectDB() {
		try {
			connection = DriverManager.getConnection(url, "root", "");
			System.out.println("Successfully connect to Mysql!");
			
		} catch (SQLException e) {
			System.out.println("Failed to connect to Mysql!");
			e.printStackTrace();
		}
	}
	
	public void initDB() throws Exception {			
			statement = connection.createStatement();			
			statement.close();
		}
}
