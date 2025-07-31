package marchcat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import marchcat.pictures.DownloadService;
import marchcat.pictures.Picture;
import marchcat.users.AccountService;
import marchcat.users.Logged;
import marchcat.users.UserRepository;

@Controller
public class AccountController {

	private Logged logged;
	private final AccountService accountService;
	private final DownloadService downloadService;

	public AccountController(UserRepository userRepository, AccountService accountService,
			DownloadService downloadService, Logged logged) {
		this.accountService = accountService;
		this.downloadService = downloadService;
		this.logged = logged;
	}

	@GetMapping("/account")
	public String accountPage(Model model) {
		if (logged.getUsername() == null) {

			return "redirect:/main";

		}
		// TODO
		int userId = logged.getId();

		Picture[] picturesOfAccount = accountService.getAccountPictures(userId);

		model.addAttribute("picturesOfAccount", picturesOfAccount);

		return "account.html";
	}

	@DeleteMapping("/account/{picLink}")
	public ResponseEntity<String> deletePicture(@PathVariable String picLink) {
		
		if (logged.getUsername() == null) {
			return ResponseEntity.status(405).body("");
		}
			//TODO
			Picture pic = downloadService.link(picLink);
			int picId = pic.getId();
			
			if(accountService.getPictureFromAccount(logged.getId(), picId)) {
				accountService.deletePicture(picId);
			}
		return ResponseEntity.status(200).body("");
	}
}
