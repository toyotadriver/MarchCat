package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import marchcat.users.RegisterService;
import marchcat.users.exception.FailedToRegisterException;

@Controller
public class RegisterController {

  private final RegisterService registerService;
	
	public RegisterController(RegisterService registerService) {
		this.registerService = registerService;
	}

	@GetMapping("/register")
	public String registerGet() {
		return "register.html";
	}
	
	@PostMapping("/register")
	public String registerPost(
			@RequestParam String username,
			@RequestParam String password,
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
			return "redirect.html";
		} else {
			model.addAttribute("message", registerService.message);
			return "register.html";
		}
		
	}
}
