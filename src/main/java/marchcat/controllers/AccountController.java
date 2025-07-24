package marchcat.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import marchcat.pictures.Picture;
import marchcat.users.AccountService;
import marchcat.users.Logged;
import marchcat.users.UserRepository;

@Controller
public class AccountController {
	
	private Logged logged;
	private final UserRepository userRepository;
	private final AccountService accountService;
	
	public AccountController(
			UserRepository userRepository,
			AccountService accountService,
			Logged logged) {
		this.userRepository = userRepository;
		this.accountService = accountService;
		this.logged = logged;
	}

	@GetMapping("/account")
	public String accountPage(Model model) {
		if(logged.getUsername() == null) {
			
			return "redirect:/main";
			
		}
		//TODO
		int userId = logged.getId();
		
		Picture[] picturesOfAccount = accountService.getAccountPictures(userId);
		
		System.out.println("User's 0 picture name: " + picturesOfAccount[0].getRnd_name());
		
		model.addAttribute(model);
		
		return "account.html";
	}
	
	
}
