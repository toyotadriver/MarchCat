package marchcat.users;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


/**
 * Session-scope bean used for simple authentication
 */
@Component
@SessionScope
public class Logged {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
