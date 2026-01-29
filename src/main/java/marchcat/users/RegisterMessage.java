package marchcat.users;

public enum RegisterMessage {
	USERNAME_WRONG_LENGTH("Username must be from 4 to 20 chars long!"),
	PASSWORD_WRONG_LENGTH("Password must be from 4 to 30 chars long!"),
	ALREADY_TAKEN("This username is already taken!"),
	OK("Valid.");
	
	private final String message;
	
	private RegisterMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
}
