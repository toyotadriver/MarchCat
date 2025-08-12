package marchcat;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import marchcat.users.LoginProcessor;
import marchcat.users.User;
import marchcat.users.UserRepository;
import marchcat.util.HashGen;



@SpringBootTest
public class LoginTest {
	
	UserRepository userRepository = mock(UserRepository.class);
	
	@Test
	@DisplayName("Test login as tralalelo tralala")
	public void loginTest() {
		
		
		LoginProcessor loginProcessor = new LoginProcessor(userRepository);
		
		String username = "tralalelo";
		String password = "tralala";
		String hashedPassword = HashGen.generatePassHash(password);
		
		User user = new User();
		user.setUsername(username);
		user.setId(2);
		user.setRole(0);
		
		when(userRepository.findUserByNameAndPassword(username, hashedPassword)).thenReturn(user);
		
		assertTrue(loginProcessor.login(username, password));
	}
	
	
}
