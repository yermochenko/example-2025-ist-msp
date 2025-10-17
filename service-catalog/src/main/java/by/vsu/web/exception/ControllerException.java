package by.vsu.web.exception;

public class ControllerException extends Exception {
	protected ControllerException() {}

	public ControllerException(String message) {
		super(message);
	}
}
