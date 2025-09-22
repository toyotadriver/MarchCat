package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import marchcat.users.Logged;

@Controller
public class MainController {
	
	Logged logged;
	
	public MainController(Logged logged) {
		this.logged = logged;
	}

	@GetMapping("/main")
	public String main(Model model) {
		String l = logged.getUsername();
		boolean loggedIn = (l != null);
		model.addAttribute("loggedIn", loggedIn);
		return "main.html";
	}
}
