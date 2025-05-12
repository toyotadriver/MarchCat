package marchcat.users;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.util.HashGen;

@Component
@RequestScope
public class LoginProcessor {

	private UserRepository userRepository;
	private User user;
	
	public LoginProcessor(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public boolean login(String username, String password) {
		
		String hashedPW = HashGen.generatePassHash(password);
		
		User user = userRepository.findUserByNameAndPassword(username, hashedPW);
		
		if(user != null) {
			this.user = user;
			return true;
		} else {
			return false;
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
