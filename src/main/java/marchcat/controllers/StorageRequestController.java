package marchcat.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.pictures.UploadInitResponse;
import marchcat.pictures.UploadService;
import marchcat.security.TokenException;
import marchcat.security.TokenManager;
import marchcat.util.LoggingAspect;
@Controller
public class StorageRequestController {

  private final UploadService uploadService;
  private final TokenManager tokenManager;
	
	public StorageRequestController(UploadService uploadService, TokenManager tokenManager) {
		this.uploadService = uploadService;
		this.tokenManager = tokenManager;
	}
	
	@GetMapping("/upload")
	public String getUpload(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			tokenManager.validateAccess(request, response).get();
			return "upload.html";
			
		} catch (TokenException e) {
			LoggingAspect.log("Failed to get username from the token: " + e.getMessage());
		}
		
		return "redirect:/main";
		
	}
	
	@PostMapping("/requestUpload")
	public UploadInitResponse postUpload(
			//@RequestParam("file") MultipartFile file,
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
