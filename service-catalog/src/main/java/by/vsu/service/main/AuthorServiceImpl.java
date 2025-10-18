package by.vsu.service.main;

import by.vsu.domain.Author;
import by.vsu.repository.AuthorRepository;
import by.vsu.repository.RepositoryException;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;

import java.util.List;
import java.util.Optional;

public class AuthorServiceImpl extends BaseServiceImpl implements AuthorService {
	private AuthorRepository authorRepository;

	public void setAuthorRepository(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Override
	public List<Author> findAll() throws ServiceException {
		try {
			getTransactionManager().startTransaction();
			List<Author> authors = authorRepository.readAll();
			getTransactionManager().commitTransaction();
			return authors;
		} catch(RepositoryException e) {
			try { getTransactionManager().rollbackTransaction(); } catch(RepositoryException ignored) {}
			throw new ServiceException(e);
		}
	}

	@Override
	public Optional<Author> findById(long id) throws ServiceException {
		try {
			getTransactionManager().startTransaction();
			Optional<Author> author = authorRepository.read(id);
			getTransactionManager().commitTransaction();
			return author;
		} catch(RepositoryException e) {
			try { getTransactionManager().rollbackTransaction(); } catch(RepositoryException ignored) {}
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean save(Author author) throws ServiceException {
		try {
			getTransactionManager().startTransaction();
			boolean result = true;
			if(author.getId() == null) {
				// create
				Long id = authorRepository.create(author);
				author.setId(id);
			} else {
				// update
				Optional<Author> savedAuthor = authorRepository.read(author.getId());
				if(savedAuthor.isPresent()) {
					authorRepository.update(author);
				} else {
					result = false;
				}
			}
			getTransactionManager().commitTransaction();
			return result;
		} catch(RepositoryException e) {
			try { getTransactionManager().rollbackTransaction(); } catch(RepositoryException ignored) {}
			throw new ServiceException(e);
		}
	}

	@Override
	public Optional<Author> delete(long id) throws ServiceException {
		try {
			getTransactionManager().startTransaction();
			Optional<Author> author = authorRepository.read(id);
			if(author.isPresent()) {
				authorRepository.delete(id);
			}
			getTransactionManager().commitTransaction();
			return author;
		} catch(RepositoryException e) {
			try { getTransactionManager().rollbackTransaction(); } catch(RepositoryException ignored) {}
			throw new ServiceException(e);
		}
	}
}
