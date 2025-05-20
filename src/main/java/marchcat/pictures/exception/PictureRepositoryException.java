package marchcat.pictures.exception;

public class PictureRepositoryException extends RuntimeException{

	private static final long serialVersionUID = 38199863384445774L;

	public PictureRepositoryException(String message) {
		super(message);
	}
	
	public PictureRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
