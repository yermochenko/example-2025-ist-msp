package by.vsu.service.main;

import by.vsu.config.util.Decorated;
import by.vsu.domain.Author;
import by.vsu.repository.AuthorRepository;
import by.vsu.repository.RepositoryException;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;

import java.util.List;
import java.util.Optional;

@Decorated
public class AuthorServiceImpl extends BaseServiceImpl implements AuthorService {
	private AuthorRepository authorRepository;

	public void setAuthorRepository(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Transaction
	@Override
	public List<Author> findAll() throws ServiceException {
		try {
			return authorRepository.readAll();
		} catch(RepositoryException e) {
			throw new ServiceException(e);
		}
	}

	@Transaction
	@Override
	public Optional<Author> findById(long id) throws ServiceException {
		try {
			return authorRepository.read(id);
		} catch(RepositoryException e) {
			throw new ServiceException(e);
		}
	}

	@Transaction
	@Override
	public boolean save(Author author) throws ServiceException {
		try {
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
			return result;
		} catch(RepositoryException e) {
			throw new ServiceException(e);
		}
	}

	@Transaction
	@Override
	public Optional<Author> delete(long id) throws ServiceException {
		try {
			Optional<Author> author = authorRepository.read(id);
			if(author.isPresent()) {
				authorRepository.delete(id);
			}
			return author;
		} catch(RepositoryException e) {
			throw new ServiceException(e);
		}
	}
}
