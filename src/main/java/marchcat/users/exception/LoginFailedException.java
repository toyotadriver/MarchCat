package marchcat.users.exception;

public class LoginFailedException extends Exception {
	
	private static final long serialVersionUID = 8508089262536566705L;

	public LoginFailedException(String message) {
		super(message);
	}
	
	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
