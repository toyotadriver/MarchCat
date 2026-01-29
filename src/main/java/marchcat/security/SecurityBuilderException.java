package marchcat.security;

public class SecurityBuilderException extends RuntimeException {

	private static final long serialVersionUID = -8420793301868004275L;
	
	public SecurityBuilderException(String message) {
		super(message);
	}
	
	public SecurityBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

}
