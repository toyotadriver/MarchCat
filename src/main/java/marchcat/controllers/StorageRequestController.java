package marchcat.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.pictures.UploadInitResponse;
import marchcat.pictures.DataService;
import marchcat.security.TokenException;
import marchcat.security.TokenManager;
import marchcat.util.LoggingAspect;
@Controller
public class StorageRequestController {

  private final DataService uploadService;
  private final TokenManager tokenManager;
	
	public StorageRequestController(DataService uploadService, TokenManager tokenManager) {
		this.uploadService = uploadService;
		this.tokenManager = tokenManager;
	}
	
	@GetMapping("/upload")
	public String getUpload(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			tokenManager.validateAccess(request, response);
			return "upload.html";
			
		} catch (TokenException e) {
			LoggingAspect.log("Failed to get username from the token: " + e.getMessage());
		}
		
		return "redirect:/main";
		
	}
	
	@ResponseBody //need this because this is not RestController. Return is the response body
	@PostMapping("/upload")
	public UploadInitResponse postUpload(
			//@RequestParam("file") MultipartFile file, it is not needed now
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) {
		
		
		try {
			Optional<String> token = tokenManager.validateAccess(request, response);
			String userName = tokenManager.getUsernameFromToken(token.get());
			return uploadService.processUploadInitResponse(userName);
		} catch (TokenException e) {
			LoggingAspect.log("Failed to validate token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
	}
}
