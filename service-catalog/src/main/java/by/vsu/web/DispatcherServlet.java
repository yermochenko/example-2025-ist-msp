package by.vsu.web;

import by.vsu.config.util.ControllerContainer;
import by.vsu.config.util.IocContainer;
import by.vsu.config.util.ValidatorContainer;
import by.vsu.domain.Entity;
import by.vsu.web.exception.BadRequestException;
import by.vsu.web.exception.ControllerException;
import by.vsu.web.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(DispatcherServlet.URI_PREFIX + "/*")
public class DispatcherServlet extends HttpServlet {
	public static final String URI_PREFIX = "/api";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp, controller -> {
			Object result = controller.get(req);
			HttpHelper.sendObject(resp, HttpServletResponse.SC_OK, result);
		});
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
		process(req, resp, controller -> {
			String idParam = req.getParameter("id");
			if(idParam != null) {
				try {
					Object result = controller.delete(Long.parseLong(idParam));
					HttpHelper.sendObject(resp, HttpServletResponse.SC_OK, result);
				} catch(NumberFormatException e) {
					throw new NotFoundException();
				}
			} else {
				throw new BadRequestException(new HttpHelper.Error("id parameter is required"));
			}
		});
	}

	private static void save(HttpServletRequest req, HttpServletResponse resp, boolean create) throws IOException {
		process(req, resp, controller -> {
			try {
				Entity entity = HttpHelper.parse(req, controller.entityClass());
				Validator<Entity> validator = ValidatorContainer.get(controller.entityClass());
				validator.validate(entity);
				if(create) {
					entity.setId(null);
					controller.post(entity);
					HttpHelper.sendObject(resp, HttpServletResponse.SC_CREATED, entity);
				} else {
					if(entity.getId() != null) {
						controller.put(entity);
						resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
					} else {
						throw new BadRequestException(new HttpHelper.EntityError("Invalid JSON", List.of(new HttpHelper.EntityError.PropertyError("id", "property is required"))));
					}
				}
			} catch(JsonProcessingException e) {
				throw new BadRequestException(new HttpHelper.Error(e.getMessage()));
			}
		});
	}

	private static void process(HttpServletRequest req, HttpServletResponse resp, RequestHandler handler) throws IOException {
		try {
			IocContainer ioc = new IocContainer();
			String uri = req.getRequestURI().substring(req.getContextPath().length() + URI_PREFIX.length());
			Class<Controller<Entity>> controllerClass = ControllerContainer.get(uri).orElseThrow(NotFoundException::new);
			Controller<Entity> controller = ioc.get(controllerClass);
			handler.process(controller);
		} catch(BadRequestException e) {
			HttpHelper.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getError());
		} catch(NotFoundException e) {
			HttpHelper.sendError(resp, HttpServletResponse.SC_NOT_FOUND, new HttpHelper.Error("Nothing found"));
		} catch(ControllerException e) {
			HttpHelper.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new HttpHelper.Error(e.getMessage()));
		}
	}

	private interface RequestHandler {
		void process(Controller<Entity> controller) throws ControllerException, IOException;
	}
}
