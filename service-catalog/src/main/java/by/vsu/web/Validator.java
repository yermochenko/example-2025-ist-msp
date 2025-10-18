package by.vsu.web;

import by.vsu.domain.Entity;
import by.vsu.web.exception.BadRequestException;

public interface Validator<T extends Entity> {
	void validate(T entity) throws BadRequestException;
}
