package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import marchcat.users.LoginProcessor;

@Controller
public class LoginController {
	
	private boolean loggedIn = false;
	private LoginProcessor loginProcessor;
	
	public LoginController(LoginProcessor loginProcessor) {
		this.loginProcessor = loginProcessor;
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		return "login.html";
	}
	
	@PostMapping("/login")
	public String enterLogin(Model model, 
			@RequestParam String username,
			@RequestParam String password) {
		
		loggedIn = loginProcessor.login(username, password);
		
		if(!loggedIn) {
			model.addAttribute("message", "Login or password is incorrect");
			return "login.html";
		} else {
			return "redirect:/main";
		}
	}
}
