package by.vsu.web;

import by.vsu.domain.Entity;
import by.vsu.web.exception.ControllerException;
import jakarta.servlet.http.HttpServletRequest;

public interface Controller<T extends Entity> {
	Class<T> entityClass();

	/**
	 * Обрабатывает HTTP-запрос методом GET
	 * @param req класс с HTTP-запросом
	 * @return объект, который необходимо отправить клиенту в JSON (статус 200 OK)
	 * @throws by.vsu.web.exception.NotFoundException если никакой объект не был найден (статус 404 Not Found)
	 * @throws by.vsu.web.exception.ControllerException если возникла техническая проблема при обработке запроса (статус 500 Internal Server Error)
	 */
	Object get(HttpServletRequest req) throws ControllerException;

	/**
	 * Обрабатывает HTTP-запрос методом POST
	 * @param entity объект, полученный от клиента и который необходимо сохранить
	 * @throws by.vsu.web.exception.ControllerException если возникла техническая проблема при обработке запроса (статус 500 Internal Server Error)
	 */
	void post(T entity) throws ControllerException;

	/**
	 * Обрабатывает HTTP-запрос методом PUT
	 * @param entity объект, полученный от клиента и который необходимо сохранить
	 * @throws by.vsu.web.exception.NotFoundException если идентификатор полученного объекта не найден (статус 404 Not Found)
	 * @throws by.vsu.web.exception.ControllerException если возникла техническая проблема при обработке запроса (статус 500 Internal Server Error)
	 */
	void put(T entity) throws ControllerException;

	/**
	 * Обрабатывает HTTP-запрос методом DELETE
	 * @param id идентификатор объекта, который необходимо удалить
	 * @return удалённый объект
	 * @throws by.vsu.web.exception.NotFoundException если идентификатор объекта не найден (статус 404 Not Found)
	 * @throws by.vsu.web.exception.ControllerException если возникла техническая проблема при обработке запроса (статус 500 Internal Server Error)
	 */
	T delete(Long id) throws ControllerException;
}
