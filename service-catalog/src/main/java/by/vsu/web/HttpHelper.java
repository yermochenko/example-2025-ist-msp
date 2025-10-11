package by.vsu.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

final public class HttpHelper {
	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());

	private HttpHelper() {}

	public static <T> T parse(HttpServletRequest req, Class<T> type) throws IOException {
		return mapper.readValue(req.getInputStream(), type);
	}

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

	public static class EntityError extends Error {
		private final List<PropertyError> errors;

		public EntityError(String message, List<PropertyError> errors) {
			super(message);
			this.errors = errors;
		}

		public List<PropertyError> getErrors() {
			return errors;
		}

		public static class PropertyError {
			private final String property;
			private final String message;

			public PropertyError(String property, String message) {
				this.property = property;
				this.message = message;
			}

			public String getProperty() {
				return property;
			}

			public String getMessage() {
				return message;
			}
		}
	}
}
