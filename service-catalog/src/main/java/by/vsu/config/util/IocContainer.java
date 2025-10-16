package by.vsu.config.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class IocContainer {
	private static final Map<Class<?>, Class<?>> implementations = new HashMap<>();
	private static final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();

	private final Map<Class<?>, Object> cache = new HashMap<>();

	public static void init(
			Supplier<Map<Class<?>, Class<?>>> implementationsConfiguration,
			Supplier<Map<Class<?>, List<Class<?>>>> dependenciesConfiguration
	) {
		implementations.putAll(implementationsConfiguration.get());
		dependencies.putAll(dependenciesConfiguration.get());
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> abstraction) {
		Object object = cache.get(abstraction);
		if(object == null) {
			Class<?> implementation = implementations.get(abstraction);
			if(implementation != null) {
				try {
					object = implementation.getConstructor().newInstance();
					List<Class<?>> dependencies = IocContainer.dependencies.get(implementation);
					if(dependencies != null) {
						for(Class<?> dependency : dependencies) {
							Method injector = implementation.getMethod("set" + dependency.getSimpleName(), dependency);
							injector.invoke(object, get(dependency));
						}
					}
				} catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException(abstraction.getName() + " is not registered");
			}
			cache.put(abstraction, object);
		}
		return (T) object;
	}

	public static class Registry {
		private final Map<Class<?>, Class<?>> implementations = new HashMap<>();
		private final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();

		private final Supplier<Map<Class<?>, Class<?>>> implementationsSupplier = () -> implementations;
		private final Supplier<Map<Class<?>, List<Class<?>>>> dependenciesSupplier = () -> dependencies;

		public void registerClass(Class<?> abstraction, Class<?> implementation, List<Class<?>> dependencies) {
			this.implementations.put(abstraction, implementation);
			this.dependencies.put(implementation, dependencies);
		}

		public Supplier<Map<Class<?>, Class<?>>> getImplementationsSupplier() {
			return implementationsSupplier;
		}

		public Supplier<Map<Class<?>, List<Class<?>>>> getDependenciesSupplier() {
			return dependenciesSupplier;
		}
	}
}
