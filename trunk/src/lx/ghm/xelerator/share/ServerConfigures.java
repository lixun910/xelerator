package lx.ghm.xelerator.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfigures {
	public static final String DefaultPropertiesFileURL = "conf/xelerator.properties";
	private static ServerConfigures _instance=null;
	private static Properties prop;
	
	public String DB_IP;
	public String DB_USER;
	public String DB_PASSWORD;
	public String DB_NAME;
	public int DB_PORT;
	
	public static ServerConfigures getInstance() {
		if (_instance == null) {
			try {
				_instance = new ServerConfigures(DefaultPropertiesFileURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _instance;
	}
	
	private ServerConfigures(String PropFileName) throws IOException{
		File inputFile = new File(PropFileName);
		FileInputStream file_is = new FileInputStream(inputFile);
		this.prop = new Properties();
		prop.load(file_is);
		
		DB_IP = prop.getProperty("DB_IP", "135.252.17.192");
		DB_NAME = prop.getProperty("DB_NAME", "XELERATOR");
		DB_USER = prop.getProperty("DB_USER", "xelerator");
		DB_PASSWORD = prop.getProperty("DB_PASSWORD", "abc123");
		DB_PORT = Integer.parseInt(prop.getProperty("DB_PORT","3306").trim());
	}
}
