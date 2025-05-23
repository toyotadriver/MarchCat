package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import marchcat.users.Logged;
import marchcat.users.RegisterService;
import marchcat.users.exception.FailedToRegisterException;

@Controller
public class RegisterController {
	
  private final RegisterService registerService;
  private Logged logged;
	
	public RegisterController(
			RegisterService registerService,
			Logged logged) {
		this.registerService = registerService;
		this.logged = logged;
	}

	@GetMapping("/register")
	public String registerGet() {
		if(logged.getUsername() == null) {
			return "register.html";
		} else {
			return "redirect.html";
		}
		
	}
	
	@PostMapping("/register")
	public String registerPost(
			@RequestHeader String username,
			@RequestHeader String password,
			Model model) {
		
		registerService.setUsername(username);
		registerService.setPassword(password);
		boolean success;
		
		try {
			success = registerService.process();
		} catch (FailedToRegisterException e) {
			success = false;
			e.printStackTrace();
		}
		

		if(success){
			logged.setUsername(username);
			return "redirect.html";
		} else {
			model.addAttribute("message", registerService.message);
			return "register.html";
		}
		
	}
}
