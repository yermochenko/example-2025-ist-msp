package by.vsu.web.exception;

import by.vsu.web.HttpHelper;

public class BadRequestException extends ControllerException {
	private final HttpHelper.Error error;

	public BadRequestException(HttpHelper.Error error) {
		this.error = error;
	}

	public HttpHelper.Error getError() {
		return error;
	}
}
