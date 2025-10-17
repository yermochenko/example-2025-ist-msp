package by.vsu.web.controller;

import by.vsu.domain.Author;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;
import by.vsu.web.Controller;
import by.vsu.web.exception.ControllerException;
import by.vsu.web.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public class AuthorController implements Controller<Author> {
	private AuthorService authorService;

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	@Override
	public Class<Author> entityClass() {
		return Author.class;
	}

	@Override
	public Object get(HttpServletRequest req) throws ControllerException {
		try {
			String idParam = req.getParameter("id");
			if(idParam != null) {
				try {
					return authorService.findById(Long.parseLong(idParam)).orElseThrow(NotFoundException::new);
				} catch(NumberFormatException e) {
					throw new NotFoundException();
				}
			} else {
				return authorService.findAll();
			}
		} catch(ServiceException e) {
			throw new ControllerException(e.getMessage());
		}
	}

	@Override
	public void post(Author author) throws ControllerException {
		try {
			authorService.save(author);
		} catch(ServiceException e) {
			throw new ControllerException(e.getMessage());
		}
	}

	@Override
	public void put(Author author) throws ControllerException {
		try {
			if(!authorService.save(author)) {
				throw new NotFoundException();
			}
		} catch(ServiceException e) {
			throw new ControllerException(e.getMessage());
		}
	}

	@Override
	public Author delete(Long id) throws ControllerException {
		try {
			return authorService.delete(id).orElseThrow(NotFoundException::new);
		} catch(ServiceException e) {
			throw new ControllerException(e.getMessage());
		}
	}
}
