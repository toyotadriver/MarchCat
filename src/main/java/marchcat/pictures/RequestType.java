package marchcat.pictures;

/**
 * Prefix {@link Enum} string for putting token to Redis.
 */
public enum RequestType {
	POST("post"),
	DELETE("delete"),
	UPDATE("update");

	public String requestPrefix;
	
	private RequestType(String requestPrefix) {
		this.requestPrefix = requestPrefix;
	}
}
