package by.vsu.service;

import by.vsu.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
	List<Author> findAll() throws ServiceException;

	Optional<Author> findById(long id) throws ServiceException;

	boolean save(Author author) throws ServiceException;

	Optional<Author> delete(long id) throws ServiceException;
}
