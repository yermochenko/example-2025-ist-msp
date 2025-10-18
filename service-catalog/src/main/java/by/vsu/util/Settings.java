package by.vsu.util;

import java.io.IOException;
import java.util.Properties;

public class Settings {
	private static final Properties properties = new Properties();

	public static void init() throws IOException {
		try(var settingsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("settings.properties")) {
			properties.load(settingsStream);
		}
	}

	public static String getProperty(Key key) {
		return properties.getProperty(key.getName());
	}

	public enum Key {
		JDBC_DRIVER("jdbc.driver"),
		JDBC_URL("jdbc.url"),
		JDBC_USERNAME("jdbc.username"),
		JDBC_PASSWORD("jdbc.password");

		private final String name;
		Key(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
