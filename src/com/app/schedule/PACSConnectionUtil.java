package com.app.schedule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class PACSConnectionUtil {

	public static Connection getConnection(String configfile){
		PACSConfiguaration config = PACSConfiguaration.getInstance(configfile);
		try {
			Class.forName(config.getConfigValue("/config/connection/driver"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Connection conn = null;
		DriverManager.setLoginTimeout(5);
		try {

			String URL = config.getConfigValue("/config/connection/url");
			String user = config.getConfigValue("/config/connection/user");
			String password = config.getConfigValue("/config/connection/password");
			conn = DriverManager.getConnection(URL,user,password);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
		
	}
}
