package by.vsu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

final public class HttpHelper {
	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());

	private HttpHelper() {}

	public static void sendObject(HttpServletResponse resp, int status, Object object) throws IOException {
		send(resp, status, object);
	}

	public static void sendError(HttpServletResponse resp, int status, Error error) throws IOException {
		send(resp, status, error);
	}

	private static void send(HttpServletResponse resp, int status, Object object) throws IOException {
		resp.setStatus(status);
		resp.setContentType("application/json");
		mapper.writeValue(resp.getOutputStream(), object);
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
