package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.security.TokenManager;

@Controller
public class LogoutController {

	private TokenManager tokenManager;
	
	public LogoutController(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		//FIXME хз, посмотрим
		if(tokenManager.validateAccess(request, response)) {
			tokenManager.putExpiredTokens(request, response);
		}
		return "redirect:/main";
	}
	
}
