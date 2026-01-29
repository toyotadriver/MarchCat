package marchcat.users.exception;

public class FailedToRegisterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FailedToRegisterException(String message) {
		super(message);
	}
	
	public FailedToRegisterException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
