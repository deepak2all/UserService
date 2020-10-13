package au.com.deepak.user.exception;

public class IncorrectInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncorrectInputException(String message) {
		super(message);
	}
	
	public IncorrectInputException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
