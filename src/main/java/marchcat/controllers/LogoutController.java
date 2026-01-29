package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import marchcat.users.Logged;

@Controller
public class LogoutController {

	private Logged logged;
	
	public LogoutController(Logged logged) {
		this.logged = logged;
	}
	
	@GetMapping("/logout")
	public String logout() {
		//TODO DELETE ACCESS_TOKEN and REFRESH_TOKEN!
		if(logged.getUsername() != null) {
			logged.setUsername(null);
			logged.setId(-1);
		}
		return "redirect:/main";
	}
	
}
