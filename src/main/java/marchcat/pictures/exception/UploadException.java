package marchcat.pictures.exception;

public class UploadException extends RuntimeException {

	private static final long serialVersionUID = -1915774824949338281L;

	
	public UploadException(String message) {
		super(message);
	}
	
	public UploadException(String message, Throwable cause) {
		super(message, cause);
	}
}
