package marchcat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.security.TokenManager;
import marchcat.users.LoginProcessor;
import marchcat.users.User;
import marchcat.users.exception.LoginFailedException;

@Controller
public class LoginController {

	private LoginProcessor loginProcessor;
	TokenManager tokenManager;

	public LoginController(LoginProcessor loginProcessor, TokenManager tokenManager) {
		
		this.tokenManager = tokenManager;
		this.loginProcessor = loginProcessor;
	}

	@GetMapping("/login")
	public String viewLoginPage(HttpServletRequest request, HttpServletResponse response) {
		if(tokenManager.validateAccess(request, response)) {
			return "redirect:/main";
		}
		
		return "login.html";
	}
	

	@PostMapping("/login")
	public ResponseEntity<String> postLogin(@RequestHeader String username,
			@RequestHeader String password,
			HttpServletRequest request,
			HttpServletResponse response) {

		User user;
		try {
			user = loginProcessor.login(username, password);

			tokenManager.putRefreshTokenToResponse(request, response, user);
			tokenManager.putAccessTokenToResponse(response, user);
			
			return ResponseEntity
					.status(200)
					.body(null);
			
		} catch (LoginFailedException e) {
			return ResponseEntity
					.status(400)
					.body("Username or password is invalid");
		}

	}
}
