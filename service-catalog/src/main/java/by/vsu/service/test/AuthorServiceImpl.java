package by.vsu.service.test;

import by.vsu.domain.Author;
import by.vsu.service.AuthorService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorServiceImpl implements AuthorService {
	private static final Map<Long, Author> authors = new ConcurrentHashMap<>();

	static {
		authors.put(1L, build(1L, "Александр", "Сергеевич", "Пушкин", 1799, 1837));
		authors.put(2L, build(2L, "Лев", "Николаевич", "Толстой", 1828, 1910));
		authors.put(3L, build(3L, "Джек", null, "Лондон", 1876, 1916));
		authors.put(4L, build(4L, "Сергей", "Васильевич", "Лукьяненко", 1968, null));
		authors.put(5L, build(5L, "Джоан", null, "Роулинг", 1965, null));
	}

	@Override
	public List<Author> findAll() {
		return authors.values().stream()
				.sorted((author1, author2) -> author1.getLastName().compareToIgnoreCase(author2.getLastName()))
				.toList();
	}

	@Override
	public Optional<Author> findById(long id) {
		return Optional.ofNullable(authors.get(id));
	}

	@Override
	public boolean save(Author author) {
		if(author.getId() == null) {
			// create
			Long id = authors.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
			author.setId(id);
			authors.put(id, author);
			return true;
		} else {
			// update
			return authors.computeIfPresent(author.getId(), (key, value) -> author) != null;
		}
	}

	@Override
	public Optional<Author> delete(long id) {
		return Optional.ofNullable(authors.remove(id));
	}

	private static Author build(Long id, String firstName, String middleName, String lastName, Integer birthYear, Integer deathYear) {
		Author author = new Author();
		author.setId(id);
		author.setFirstName(firstName);
		author.setMiddleName(middleName);
		author.setLastName(lastName);
		author.setBirthYear(birthYear);
		author.setDeathYear(deathYear);
		return author;
	}
}
