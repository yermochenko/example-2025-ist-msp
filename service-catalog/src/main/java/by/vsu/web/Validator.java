package by.vsu.web;

import by.vsu.domain.Entity;

public interface Validator<T extends Entity> {
	HttpHelper.EntityError validate(T entity);
}
