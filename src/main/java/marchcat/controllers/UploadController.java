package marchcat.controllers;

import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

import marchcat.users.Logged;

@Controller
public class UploadController {
	
	private Logged logged;
	
	public UploadController(Logged logged) {
		this.logged = logged;
	}
	
	@GetMapping("/upload")
	public String getUpload(Model model) {
		return "upload.html";
	}
	
	@PostMapping("/upload")
	public String postUpload(@RequestPart Part part, Model model) {
		
		if(logged.getUsername() != null) {
			
		}
		return "upload.html";
	}
}
