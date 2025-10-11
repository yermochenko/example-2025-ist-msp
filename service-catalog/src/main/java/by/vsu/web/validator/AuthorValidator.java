package by.vsu.web.validator;

import by.vsu.domain.Author;
import by.vsu.web.HttpHelper;
import by.vsu.web.Validator;

import java.util.ArrayList;
import java.util.List;

public class AuthorValidator implements Validator<Author> {
	@Override
	public HttpHelper.EntityError validate(Author author) {
		List<HttpHelper.EntityError.PropertyError> errors = new ArrayList<>();
		if(author.getFirstName() == null) {
			errors.add(new HttpHelper.EntityError.PropertyError("firstName", "property is required"));
		} else if(author.getFirstName().isBlank()) {
			errors.add(new HttpHelper.EntityError.PropertyError("firstName", "property cannot be blank"));
		}
		if(author.getLastName() == null) {
			errors.add(new HttpHelper.EntityError.PropertyError("lastName", "property is required"));
		} else if(author.getLastName().isBlank()) {
			errors.add(new HttpHelper.EntityError.PropertyError("lastName", "property cannot be blank"));
		}
		if(author.getMiddleName().isPresent() && author.getMiddleName().get().isBlank()) {
			errors.add(new HttpHelper.EntityError.PropertyError("middleName", "property cannot be blank"));
		}
		if(author.getBirthYear() == null) {
			errors.add(new HttpHelper.EntityError.PropertyError("birthYear", "property is required"));
		} else if(author.getDeathYear().isPresent() && author.getBirthYear() > author.getDeathYear().get()) {
			errors.add(new HttpHelper.EntityError.PropertyError("deathYear", "property should be greater than property birthYear"));
		}
		if(!errors.isEmpty()) {
			return new HttpHelper.EntityError("Invalid JSON for author", errors);
		} else {
			return null;
		}
	}
}
