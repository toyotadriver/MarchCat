package marchcat.users;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.users.exception.FailedToRegisterException;
import marchcat.util.HashGen;

@Service
@RequestScope
public class RegisterService {
	
	private String username;
	private String password;
	
	private final UserRepository userRepository;

	private boolean valid = false;
	public String message = "OK";
	
	public RegisterService(
			UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	/**
	 * Register the user.
	 * @return boolean success of the operation.
	 * @throws FailedToRegisterException
	 */
	public boolean process() throws FailedToRegisterException {
		
		valid = validateLogin();
		
		if (valid) {
			String hashedPW = HashGen.generatePassHash(password);
			
			boolean success = userRepository.insertUser(username, hashedPW);
			
			if(success) {
				return true;
			} else {
				throw new FailedToRegisterException("Failed to register user.");
			}
		}
		return false;
	}

	/**
	 * Validate login
	 * @return success
	 */
	private boolean validateLogin() {
		int unl = username.length();
		int pwl = password.length();
		if(unl < 4 || unl > 20) {
			message = "Username must be between 4 and 20 chars long!";
			return false;
		} else if(pwl < 4 || pwl > 30) {
			message = "Password must be between 4 and 30 chars long!";
			return false;
		} else {
			return true;
		}
		
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
