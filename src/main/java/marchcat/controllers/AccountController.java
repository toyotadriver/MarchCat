package marchcat.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import marchcat.pictures.DownloadService;
import marchcat.pictures.LinkRepository;
import marchcat.pictures.Picture;
import marchcat.users.AccountService;
import marchcat.users.Logged;
import marchcat.users.UserRepository;

@Controller
public class AccountController {
	
	private Logged logged;
	private final UserRepository userRepository;
	private final AccountService accountService;
	private final DownloadService downloadService;
	
	public AccountController(
			UserRepository userRepository,
			AccountService accountService,
			DownloadService downloadService,
			Logged logged) {
		this.userRepository = userRepository;
		this.accountService = accountService;
		this.downloadService = downloadService;
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
		
		
		
		System.out.println("User's 0 picture link: " + picturesOfAccount[0].getLink());
		
		model.addAttribute("picturesOfAccount", picturesOfAccount);
		
		return "account.html";
	}
	
	
}
