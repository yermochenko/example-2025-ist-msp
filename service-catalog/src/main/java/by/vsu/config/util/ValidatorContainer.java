package by.vsu.config.util;

import by.vsu.domain.Entity;
import by.vsu.web.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ValidatorContainer {
	private static final Map<Class<? extends Entity>, Validator<? extends Entity>> validators = new HashMap<>();

	public static void init(Supplier<Map<Class<? extends Entity>, Validator<? extends Entity>>> configuration) {
		validators.putAll(configuration.get());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> Validator<T> get(Class<T> entityClass) {
		return Objects.requireNonNull((Validator<T>) validators.get(entityClass));
	}

	public static class Registry implements Supplier<Map<Class<? extends Entity>, Validator<? extends Entity>>> {
		private final Map<Class<? extends Entity>, Validator<? extends Entity>> validators = new HashMap<>();

		@Override
		public Map<Class<? extends Entity>, Validator<? extends Entity>> get() {
			return validators;
		}

		public <V extends Entity> void registerValidator(Class<V> entityClass, Validator<V> validator) {
			validators.put(entityClass, validator);
		}
	}
}
