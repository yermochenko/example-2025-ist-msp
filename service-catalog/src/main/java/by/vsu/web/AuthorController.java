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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());
		try {
			String idParam = req.getParameter("id");
			if(idParam != null) {
				try {
					Author author = authorService.findById(Long.parseLong(idParam)).orElseThrow(IllegalArgumentException::new);
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.setContentType("application/json");
					mapper.writeValue(resp.getOutputStream(), author);
				} catch(IllegalArgumentException e) {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					resp.setContentType("application/json");
					mapper.writeValue(resp.getOutputStream(), new Error("Nothing found"));
				}
			} else {
				List<Author> authors = authorService.findAll();
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("application/json");
				mapper.writeValue(resp.getOutputStream(), authors);
			}
		} catch(ServiceException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			mapper.writeValue(resp.getOutputStream(), new Error(e.getMessage()));
		}
	}

	public static class Error {
		private final String message;

		public Error(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
