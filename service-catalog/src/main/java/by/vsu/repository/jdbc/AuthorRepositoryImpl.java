package by.vsu.repository.jdbc;

import by.vsu.domain.Author;
import by.vsu.repository.AuthorRepository;
import by.vsu.repository.RepositoryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryImpl extends BaseRepository<Author> implements AuthorRepository {
	public AuthorRepositoryImpl() {
		super(
			"SELECT \"id\", \"first_name\", \"middle_name\", \"last_name\", \"birth_year\", \"death_year\" FROM \"author\" WHERE \"id\" = ?",
			"INSERT INTO \"author\"(\"first_name\", \"middle_name\", \"last_name\", \"birth_year\", \"death_year\") VALUES (?, ?, ?, ?, ?)",
			"UPDATE \"author\" SET \"first_name\" = ?, \"middle_name\" = ?, \"last_name\" = ?, \"birth_year\" = ?, \"death_year\" = ? WHERE \"id\" = ?",
			"DELETE FROM \"author\" WHERE \"id\" = ?"
		);
	}

	@Override
	public List<Author> readAll() throws RepositoryException {
		String sql = "SELECT \"id\", \"first_name\", \"middle_name\", \"last_name\", \"birth_year\", \"death_year\" FROM \"author\" ORDER BY \"last_name\", \"first_name\"";
		List<Author> authors = new ArrayList<>();
		read(sql, null, authors::add);
		return authors;
	}

	@Override
	protected Author buildFromResultSet(ResultSet resultSet) throws SQLException {
		Author author = new Author();
		author.setId(resultSet.getLong("id"));
		author.setFirstName(resultSet.getString("first_name"));
		author.setMiddleName(resultSet.getString("middle_name"));
		author.setLastName(resultSet.getString("last_name"));
		author.setBirthYear(resultSet.getInt("birth_year"));
		int deathYear = resultSet.getInt("death_year");
		if(!resultSet.wasNull()) {
			author.setDeathYear(deathYear);
		}
		return author;
	}

	@Override
	protected void fillInsertPreparedStatement(PreparedStatement statement, Author author) throws SQLException {
		statement.setString(1, author.getFirstName());
		if(author.getMiddleName().isPresent()) {
			statement.setString(2, author.getMiddleName().get());
		} else {
			statement.setNull(2, Types.CLOB);
		}
		statement.setString(3, author.getLastName());
		statement.setInt(4, author.getBirthYear());
		if(author.getDeathYear().isPresent()) {
			statement.setInt(5, author.getDeathYear().get());
		} else {
			statement.setNull(5, Types.INTEGER);
		}
	}

	@Override
	protected void fillUpdatePreparedStatement(PreparedStatement statement, Author author) throws SQLException {
		fillInsertPreparedStatement(statement, author);
		statement.setLong(6, author.getId());
	}
}
