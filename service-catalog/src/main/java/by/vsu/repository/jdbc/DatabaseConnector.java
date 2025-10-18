package by.vsu.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	private static String jdbcUrl;
	private static String user;
	private static String password;

	public static void init(String driverClass, String jdbcUrl, String user, String password) throws ClassNotFoundException {
		Class.forName(driverClass);
		DatabaseConnector.jdbcUrl = jdbcUrl;
		DatabaseConnector.user = user;
		DatabaseConnector.password = password;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, user, password);
	}
}
