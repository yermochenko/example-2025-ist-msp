package by.vsu.repository.jdbc;

import by.vsu.config.util.IocContainer;
import by.vsu.config.util.exception.IocException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory implements IocContainer.Factory<Connection> {
	@Override
	public Connection newInstance() throws IocException {
		try {
			return DatabaseConnector.getConnection();
		} catch(SQLException e) {
			throw new IocException(e);
		}
	}
}
