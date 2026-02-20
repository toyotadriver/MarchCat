package marchcat.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.pictures.DownloadService;
import marchcat.pictures.Picture;
import marchcat.security.TokenException;
import marchcat.security.TokenManager;
import marchcat.users.AccountService;
import marchcat.users.Logged;
import marchcat.users.UserRepository;
import marchcat.util.LoggingAspect;

@Controller
public class AccountController {

	private final AccountService accountService;
	private final DownloadService downloadService;
	private final TokenManager tokenManager;

	public AccountController(UserRepository userRepository, AccountService accountService,
			DownloadService downloadService, TokenManager tokenManager) {
		this.accountService = accountService;
		this.downloadService = downloadService;
		this.tokenManager = tokenManager;
	}

	@GetMapping("/account")
	public String accountPage(HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		int userId;
		String accessToken;
		try {
			
			accessToken = tokenManager.validateAccess(request, response).get();
			userId = Integer.valueOf(tokenManager.getIdFromToken(accessToken));
			
		} catch (TokenException | NumberFormatException e) {
			LoggingAspect.log("Failed to auth to get the account data: " + e.getCause().getMessage());
			return "redirect:/main";
		}

		Picture[] picturesOfAccount = accountService.getAccountPictures(userId);

		model.addAttribute("picturesOfAccount", picturesOfAccount);

		return "account.html";
	}

	
	
	@DeleteMapping("/account/{picLink}")
	public ResponseEntity<String> deletePicture(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String picLink) {
		
		//TODO this must request to MCStorage now too
		
		try {
			tokenManager.validateAccess(request, response);
		} catch (TokenException e) {
			LoggingAspect.log("Failed to auth while processing DELETE request");
			return ResponseEntity.status(405).body("");
		}
			
			//TODO
			Picture pic = downloadService.link(picLink);
			int picId = pic.getId();
			
			if(accountService.getPictureFromAccount(logged.getId(), picId)) {
				accountService.deletePicture(picId);
			}
		//204 is for "resource deleted successfully"
		return ResponseEntity.status(204).body("");
	}
}
