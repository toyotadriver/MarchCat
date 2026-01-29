package marchcat.users;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.users.exception.LoginFailedException;
import marchcat.util.HashGen;

@Component
@RequestScope
public class LoginProcessor {

	private UserRepository userRepository;
	
	public LoginProcessor(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User login(String username, String password) throws LoginFailedException {
		
		String hashedPW = HashGen.generateStringHash(password);
		
		User user = userRepository.findUserByNameAndPassword(username, hashedPW);
		
		if(user != null) {
			return user;
		} else {
			throw new LoginFailedException("Failed to login");
		}
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
	
	
}
