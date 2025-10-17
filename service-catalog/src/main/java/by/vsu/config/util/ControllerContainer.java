package by.vsu.config.util;

import by.vsu.domain.Entity;
import by.vsu.web.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ControllerContainer {
	private static final Map<String, Class<? extends Controller<?>>> controllers = new HashMap<>();

	public static void init(Supplier<Map<String, Class<? extends Controller<?>>>> configuration) {
		controllers.putAll(configuration.get());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> Optional<Class<Controller<T>>> get(String url) {
		return Optional.ofNullable((Class<Controller<T>>) controllers.get(url));
	}

	public static class Registry implements Supplier<Map<String, Class<? extends Controller<?>>>> {
		private final Map<String, Class<? extends Controller<?>>> controllers = new HashMap<>();

		@Override
		public Map<String, Class<? extends Controller<?>>> get() {
			return controllers;
		}

		public void registerController(String url, Class<? extends Controller<?>> controllerClass) {
			controllers.put(url, controllerClass);
		}
	}
}
