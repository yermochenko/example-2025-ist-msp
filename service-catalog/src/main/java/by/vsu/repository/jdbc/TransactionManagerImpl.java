package by.vsu.repository.jdbc;

import by.vsu.repository.RepositoryException;
import by.vsu.repository.TransactionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImpl implements TransactionManager {
	private Connection connection;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void startTransaction() throws RepositoryException {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public void commitTransaction() throws RepositoryException {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch(SQLException e) {
				connection = null;
			}
		}
	}

	@Override
	public void rollbackTransaction() throws RepositoryException {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch(SQLException e) {
				connection = null;
			}
		}
	}
}
