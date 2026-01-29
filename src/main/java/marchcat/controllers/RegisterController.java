package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.security.TokenManager;
import marchcat.users.LoginProcessor;
import marchcat.users.RegisterService;
import marchcat.users.User;
import marchcat.users.exception.FailedToRegisterException;
import marchcat.users.exception.LoginFailedException;

@Controller
public class RegisterController {
	
  private final RegisterService registerService;
  private final TokenManager tokenManager;
  private final LoginProcessor loginProcessor;
	
	public RegisterController(
			RegisterService registerService,
			TokenManager tokenManager,
			LoginProcessor loginProcessor) {
		this.registerService = registerService;
		this.tokenManager = tokenManager;
		this.loginProcessor = loginProcessor;
	}

	@GetMapping("/register")
	public String registerGet(HttpServletRequest request, HttpServletResponse response) {
		if(!tokenManager.validateAccess(request, response)) {
			return "register.html";
		} else {
			return "redirect.html";
		}
		
	}
	
	@PostMapping("/register")
	public String registerPost(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader String username,
			@RequestHeader String password,
			Model model) {
		System.out.println("user: " + username + " pw: " + password);
		
		String message;
		String page;
		User user;
		
		try {
			message = registerService.process(username, password);
			user = loginProcessor.login(username, password);
			
			tokenManager.putRefreshTokenToResponse(request, response, user);
			tokenManager.putAccessTokenToResponse(response, user);
			
			page = "redirect.html";
		} catch (FailedToRegisterException | LoginFailedException e) {
			message = "Failed to register the user";
			page = "register.html";
		}
		
		model.addAttribute("registerMessage", message);
		return page;
		
	}
}
