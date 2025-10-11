package by.vsu.web.controller;

import by.vsu.domain.Author;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;
import by.vsu.service.ServiceFactory;
import by.vsu.web.HttpHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/author")
public class AuthorController extends HttpServlet {
	private AuthorService authorService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			ServiceFactory factory = ServiceFactory.newInstance();
			authorService = factory.newAuthorServiceInstance();
		} catch(ServiceException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
}
