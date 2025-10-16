package by.vsu.web.controller;

import by.vsu.config.util.IocContainer;
import by.vsu.config.util.ValidatorContainer;
import by.vsu.domain.Author;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;
import by.vsu.web.HttpHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/author")
public class AuthorController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		IocContainer ioc = new IocContainer();
		AuthorService authorService = ioc.get(AuthorService.class);
		try {
			String idParam = req.getParameter("id");
			if(idParam != null) {
				try {
					Author author = authorService.findById(Long.parseLong(idParam)).orElseThrow(IllegalArgumentException::new);
					HttpHelper.sendObject(resp, HttpServletResponse.SC_OK, author);
				} catch(IllegalArgumentException e) {
					HttpHelper.sendError(resp, HttpServletResponse.SC_NOT_FOUND, new HttpHelper.Error("Nothing found"));
				}
			} else {
				List<Author> authors = authorService.findAll();
				HttpHelper.sendObject(resp, HttpServletResponse.SC_OK, authors);
			}
		} catch(ServiceException e) {
			HttpHelper.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new HttpHelper.Error(e.getMessage()));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		save(req, resp, true);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		save(req, resp, false);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		IocContainer ioc = new IocContainer();
		AuthorService authorService = ioc.get(AuthorService.class);
		String idParam = req.getParameter("id");
		if(idParam != null) {
			try {
				Author author = authorService.delete(Long.parseLong(idParam)).orElseThrow(IllegalArgumentException::new);
				HttpHelper.sendObject(resp, HttpServletResponse.SC_OK, author);
			} catch(IllegalArgumentException e) {
				HttpHelper.sendError(resp, HttpServletResponse.SC_NOT_FOUND, new HttpHelper.Error("Nothing found"));
			} catch(ServiceException e) {
				HttpHelper.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new HttpHelper.Error(e.getMessage()));
			}
		} else {
			HttpHelper.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, new HttpHelper.Error("id parameter is required"));
		}
	}

	private void save(HttpServletRequest req, HttpServletResponse resp, boolean create) throws IOException {
		try {
			Author author = HttpHelper.parse(req, Author.class);
			HttpHelper.EntityError error = ValidatorContainer.get(Author.class).validate(author);
			if(error == null) {
				IocContainer ioc = new IocContainer();
				AuthorService authorService = ioc.get(AuthorService.class);
				try {
					if(create) {
						author.setId(null);
						authorService.save(author);
						HttpHelper.sendObject(resp, HttpServletResponse.SC_CREATED, author);
					} else {
						if(author.getId() != null) {
							if(authorService.save(author)) {
								resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
							} else {
								HttpHelper.sendError(resp, HttpServletResponse.SC_NOT_FOUND, new HttpHelper.Error("Nothing found"));
							}
						} else {
							HttpHelper.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, new HttpHelper.EntityError("Invalid JSON", List.of(new HttpHelper.EntityError.PropertyError("id", "property is required"))));
						}
					}
				} catch(ServiceException e) {
					HttpHelper.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new HttpHelper.Error(e.getMessage()));
				}
			} else {
				HttpHelper.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, error);
			}
		} catch(JsonProcessingException e) {
			HttpHelper.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, new HttpHelper.Error(e.getMessage()));
		}
	}
}
