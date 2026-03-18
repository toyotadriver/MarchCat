package marchcat.unit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import marchcat.users.LoginProcessor;
import marchcat.users.User;
import marchcat.users.UserRepository;
import marchcat.users.exception.LoginFailedException;
import marchcat.util.HashGen;



@SpringBootTest
public class LoginProcessorTest {
	
	static UserRepository userRepository = mock(UserRepository.class);
	static String username = "tralalelo";
	static String password = "tralala";
	static String hashedPassword = HashGen.generateStringHash(password);
	
	@BeforeAll
	public static void prepare() {
		User user = new User();
		user.setUsername(username);
		user.setId(2);
		user.setRole(0);
		when(userRepository.findUserByNameAndPassword(username, hashedPassword)).thenReturn(user);
	}
	
	@Test
	@DisplayName("Test login as tralalelo tralala")
	public void loginTest() {
		
		
		LoginProcessor loginProcessor = new LoginProcessor(userRepository);
		
		boolean l = false;
		try {
			l = loginProcessor.login(username, password) != null;
		} catch (LoginFailedException e) {
			// TODO: handle exception
		}
		
		assertTrue(l);
	}
	
	
}
