package marchcat.storage.exception;

public class StorageException extends RuntimeException{

	private static final long serialVersionUID = 7000291772691756871L;

	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
