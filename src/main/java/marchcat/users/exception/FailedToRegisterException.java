package marchcat.users.exception;

public class FailedToRegisterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FailedToRegisterException(String message) {
		super(message);
	}
	
	public FailedToRegisterException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
