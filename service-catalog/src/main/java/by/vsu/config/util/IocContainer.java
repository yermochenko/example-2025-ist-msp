package by.vsu.config.util;

import by.vsu.config.util.exception.IocException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class IocContainer implements AutoCloseable {
	private static final Map<Class<?>, Class<?>> implementations = new HashMap<>();
	private static final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();
	private static final Map<Class<?>, Factory<?>> factories = new HashMap<>();
	private static final Map<Class<?>, Decorator> decorators = new HashMap<>();

	private final Map<Class<?>, Object> cache = new HashMap<>();

	public static void init(
			Supplier<Map<Class<?>, Class<?>>> implementationsConfiguration,
			Supplier<Map<Class<?>, List<Class<?>>>> dependenciesConfiguration,
			Supplier<Map<Class<?>, Factory<?>>> factoriesConfiguration,
			Supplier<Map<Class<?>, Decorator>> decoratorsConfiguration
	) {
		implementations.putAll(implementationsConfiguration.get());
		dependencies.putAll(dependenciesConfiguration.get());
		factories.putAll(factoriesConfiguration.get());
		decorators.putAll(decoratorsConfiguration.get());
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> abstraction) throws IocException {
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
					if(implementation.getDeclaredAnnotation(Decorated.class) != null) {
						object = Proxy.newProxyInstance(
							implementation.getClassLoader(),
							new Class[] { abstraction },
							new MethodInterceptor(object)
						);
					}
				} catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			} else {
				Factory<?> factory = factories.get(abstraction);
				if(factory != null) {
					object = factory.newInstance();
				} else {
					throw new RuntimeException(abstraction.getName() + " is not registered");
				}
			}
			cache.put(abstraction, object);
		}
		return (T) object;
	}

	@Override
	public void close() {
		for(Object obj : cache.values()) {
			if(obj instanceof AutoCloseable resource) {
				try { resource.close(); } catch (Exception ignored) {}
			}
		}
	}

	public interface Factory<T> {
		T newInstance() throws IocException;
	}

	public interface Decorator {
		Object execute(Object object, Method method, Object[] args) throws IocException;
	}

	public static class Registry {
		private final Map<Class<?>, Class<?>> implementations = new HashMap<>();
		private final Map<Class<?>, List<Class<?>>> dependencies = new HashMap<>();
		private final Map<Class<?>, Factory<?>> factories = new HashMap<>();
		private final Map<Class<?>, Decorator> decorators = new HashMap<>();

		private final Supplier<Map<Class<?>, Class<?>>> implementationsSupplier = () -> implementations;
		private final Supplier<Map<Class<?>, List<Class<?>>>> dependenciesSupplier = () -> dependencies;
		private final Supplier<Map<Class<?>, Factory<?>>> factoriesSupplier = () -> factories;
		private final Supplier<Map<Class<?>, Decorator>> decoratorsSupplier = () -> decorators;

		public void registerClass(Class<?> abstraction, Class<?> implementation, List<Class<?>> dependencies) {
			this.implementations.put(abstraction, implementation);
			this.dependencies.put(implementation, dependencies);
		}

		public void registerFactory(Class<?> abstraction, Factory<?> factory) {
			this.factories.put(abstraction, factory);
		}

		public void registerDecorator(Decorator decorator, List<Class<?>> decoratedClasses) {
			for(Class<?> decoratedClass : decoratedClasses) {
				this.decorators.put(decoratedClass, decorator);
			}
		}

		public Supplier<Map<Class<?>, Class<?>>> getImplementationsSupplier() {
			return implementationsSupplier;
		}

		public Supplier<Map<Class<?>, List<Class<?>>>> getDependenciesSupplier() {
			return dependenciesSupplier;
		}

		public Supplier<Map<Class<?>, Factory<?>>> getFactoriesSupplier() {
			return factoriesSupplier;
		}

		public Supplier<Map<Class<?>, Decorator>> getDecoratorsSupplier() {
			return decoratorsSupplier;
		}
	}

	private record MethodInterceptor(Object object) implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Class<?> clazz = object.getClass();
			Method methodImplementation = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
			Decorator decorator = decorators.get(clazz);
			if(decorator != null) {
				return decorator.execute(object, methodImplementation, args);
			} else {
				return method.invoke(object, args);
			}
		}
	}
}
