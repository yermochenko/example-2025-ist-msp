package by.vsu.config;

import by.vsu.config.util.ValidatorContainer;
import by.vsu.domain.Author;
import by.vsu.web.validator.AuthorValidator;

public class ValidatorConfiguration {
	public static void build(ValidatorContainer.Registry registry) {
		registry.registerValidator(Author.class, new AuthorValidator());
	}
}
