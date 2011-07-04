package lx.ghm.xelerator.share;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static Connection conn = null;
	private static DBConnection _instance =null;
	private static boolean isConnectionClosed = false;
	
	public static DBConnection getInstance() {
		if (_instance == null || isConnectionClosed == true) {
			try {
				_instance = new DBConnection();
				isConnectionClosed = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _instance;
	}
	
	private DBConnection() {
		try {
			ServerConfigures conf = ServerConfigures.getInstance();
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			String url = "jdbc:mysql://" + conf.DB_IP + ":" + conf.DB_PORT + "/" + conf.DB_NAME;
			String user = conf.DB_USER;
			String password = conf.DB_PASSWORD;
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public Connection getConnection(){
		return conn;
	}
	
	public void closeDB() {
		try{
			conn.close();
			isConnectionClosed = true;
		}catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
