package Server.main;

import java.awt.EventQueue;
import java.sql.SQLException;

import Server.db.DBManager;


public class StartServers {
	public static void main(String args[]) {
		DBManager dbManager = DBManager.getDBManager();
		dbManager.addDBDriver();
		dbManager.connectDB();
		try {
			dbManager.initDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServerListener listener = new ServerListener();
		if (!listener.isAlive()) {
			listener.start();
		}		
	}
}

