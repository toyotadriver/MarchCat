package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.security.TokenException;
import marchcat.security.TokenManager;

@Controller
public class MainController {
	
	TokenManager tokenManager;
	
	public MainController(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	@GetMapping("/main")
	public String main(HttpServletRequest request,
			HttpServletResponse response,
			Model model) {
		boolean loggedIn = false;
		try {
			tokenManager.validateAccess(request, response);
			loggedIn = true;
		} catch (TokenException e) {
			loggedIn = false;
		}
		model.addAttribute("loggedIn", loggedIn);
		return "main.html";
	}
}
