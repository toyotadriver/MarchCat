package marchcat.pictures.exception;

public class PictureValidateException extends Exception{
	private static final long serialVersionUID = 7284731093682707055L;

	public PictureValidateException(String message) {
		super(message);
	}
	
	public PictureValidateException(String message, Throwable cause) {
		super(message, cause);
	}
}
