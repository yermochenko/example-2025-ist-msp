package by.vsu.repository;

import by.vsu.domain.Author;

import java.util.List;

public interface AuthorRepository extends Repository<Author> {
	List<Author> readAll() throws RepositoryException;
}
