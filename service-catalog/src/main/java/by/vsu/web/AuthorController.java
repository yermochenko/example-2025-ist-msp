package by.vsu.web;

import by.vsu.domain.Author;
import by.vsu.service.AuthorService;
import by.vsu.service.ServiceException;
import by.vsu.service.ServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			List<Author> authors = authorService.findAll();
			ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			mapper.writeValue(resp.getOutputStream(), authors);
		} catch(ServiceException e) {
			throw new ServletException(e);
		}
	}
}
