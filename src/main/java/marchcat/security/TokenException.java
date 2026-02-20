package marchcat.security;

public class TokenException extends RuntimeException {

	private static final long serialVersionUID = -8420793301868004275L;
	
	public TokenException(String message) {
		super(message);
	}
	
	public TokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
