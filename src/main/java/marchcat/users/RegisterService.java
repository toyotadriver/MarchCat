package marchcat.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.users.exception.FailedToRegisterException;
import marchcat.util.HashGen;

@Service
@RequestScope
public class RegisterService {
	
	private final UserRepository userRepository;
	
	public RegisterService(
			UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	/**
	 * Register the user. Return message String or throw {@link FailedToRegisterException} if failed.
	 * @return boolean success of the operation.
	 * @throws FailedToRegisterException
	 */
	@Transactional
	public String process(String username, String password) throws FailedToRegisterException {
		
		RegisterMessage msg = validateLogin(username, password);
		
		if (msg == RegisterMessage.OK) {
			String hashedPW = HashGen.generateStringHash(password);
			
			boolean success = userRepository.insertUser(username, hashedPW);
			
			if(!success) {
				throw new FailedToRegisterException("Failed to register user.");
			}
		}
		return msg.getMessage();
	}

	/**
	 * Validate login
	 * @return success
	 */
	private RegisterMessage validateLogin(String username, String password) {
		int unl = username.length();
		int pwl = password.length();
		if(unl < 4 || unl > 20) {
			return RegisterMessage.USERNAME_WRONG_LENGTH;
		} else if(pwl < 4 || pwl > 30) {
			return RegisterMessage.PASSWORD_WRONG_LENGTH;
		} else {
			return RegisterMessage.OK;
		}
		
	}
	
	
}
