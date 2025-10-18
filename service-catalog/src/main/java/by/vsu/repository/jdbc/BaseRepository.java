package by.vsu.repository.jdbc;

import by.vsu.domain.Entity;
import by.vsu.repository.Repository;
import by.vsu.repository.RepositoryException;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

abstract public class BaseRepository<E extends Entity> implements Repository<E> {
	private final String selectSql;
	private final String insertSql;
	private final String updateSql;
	private final String deleteSql;

	private Connection connection;

	protected BaseRepository(String selectSql, String insertSql, String updateSql, String deleteSql) {
		this.selectSql = selectSql;
		this.insertSql = insertSql;
		this.updateSql = updateSql;
		this.deleteSql = deleteSql;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected Connection getConnection() {
		return connection;
	}

	@Override
	public final Long create(E entity) throws RepositoryException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			fillInsertPreparedStatement(statement, entity);
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			return resultSet.getLong(1);
		} catch(SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	@Override
	public final Optional<E> read(Long id) throws RepositoryException {
		AtomicReference<E> entity = new AtomicReference<>();
		read(selectSql, statement -> statement.setLong(1, id), entity::set);
		return Optional.ofNullable(entity.get());
	}

	@Override
	public void update(E entity) throws RepositoryException {
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(updateSql);
			fillUpdatePreparedStatement(statement, entity);
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	protected final void read(String sql, SearchCriteriaFiller filler, Consumer<E> handler) throws RepositoryException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().prepareStatement(sql);
			if(filler != null) {
				filler.fill(statement);
			}
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				E entity = buildFromResultSet(resultSet);
				handler.accept(entity);
			}
		} catch(SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try { Objects.requireNonNull(resultSet).close(); } catch(Exception ignored) {}
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	@Override
	public void delete(Long id) throws RepositoryException {
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(deleteSql);
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch(SQLException e) {
			throw new RepositoryException(e);
		} finally {
			try { Objects.requireNonNull(statement).close(); } catch(Exception ignored) {}
		}
	}

	abstract protected E buildFromResultSet(ResultSet resultSet) throws SQLException;

	abstract protected void fillInsertPreparedStatement(PreparedStatement statement, E entity) throws SQLException;

	abstract protected void fillUpdatePreparedStatement(PreparedStatement statement, E entity) throws SQLException;

	protected interface SearchCriteriaFiller {
		void fill(PreparedStatement statement) throws SQLException;
	}
}
