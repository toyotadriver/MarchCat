package marchcat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import marchcat.pictures.UploadService;
import marchcat.users.Logged;

@Controller
public class UploadController {

  private final UploadService uploadService;
	
	private Logged logged;
	
	public UploadController(Logged logged, UploadService uploadService) {
		this.logged = logged;
		this.uploadService = uploadService;
	}
	
	@GetMapping("/upload")
	public String getUpload(Model model) {
		if(logged.getUsername() != null) {
			return "upload.html";
		} else {
			return "redirect:/main";
		}
		
	}
	
	@PostMapping("/upload")
	public String postUpload(
			@RequestParam("file") MultipartFile file,
			Model model) {
		if(logged.getUsername() != null) {
			
			uploadService.process(file);
			
			model.addAttribute("message", "Your image was uploaded!");
			
			return "upload.html";
		}else {
			return "redirect:/main";
		}
		
		
	}
}
