package marchcat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import marchcat.users.Logged;
import marchcat.users.LoginProcessor;

@Controller
public class LoginController {

	private boolean loggedIn = false;
	private LoginProcessor loginProcessor;
	private Logged logged;

	public LoginController(LoginProcessor loginProcessor, Logged logged) {
		this.loginProcessor = loginProcessor;
		this.logged = logged;
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		if (logged.getUsername() == null) {
			return "login.html";
		} else {
			return "redirect:/main";
		}
	}

	/**
	 * Check for login and set Logged username to logged in username
	 * 
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */
	/*
	 * @PostMapping("/login") public String enterLogin(Model model,
	 * 
	 * @RequestHeader String username,
	 * 
	 * @RequestHeader String password) {
	 * 
	 * loggedIn = loginProcessor.login(username, password);
	 * 
	 * if(!loggedIn) { model.addAttribute("message",
	 * "Login or password is incorrect"); return "login.html"; } else {
	 * 
	 * logged.setUsername(username);
	 * 
	 * return "redirect:/main"; } }
	 */

	@PostMapping("/login")
	public ResponseEntity<String> postLogin(@RequestHeader String username, @RequestHeader String password) {

		loggedIn = loginProcessor.login(username, password);
		if (!loggedIn) {
			String message = "Incorrect login or password";

			return ResponseEntity
					.status(400)
					.body(message);
		} else {
			logged.setUsername(username);
			return ResponseEntity
					.status(200)
					.body(null);
		}

	}
}
