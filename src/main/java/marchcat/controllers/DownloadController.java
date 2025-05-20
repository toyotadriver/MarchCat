package marchcat.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import marchcat.pictures.DownloadService;
import marchcat.pictures.Picture;
import marchcat.pictures.exception.PictureRepositoryException;

@RestController
public class DownloadController {
	
	private final DownloadService downloadService;
	
	public DownloadController(DownloadService downloadService) {
		this.downloadService = downloadService;
	}

	@GetMapping("/link/{picLink}")
	public Picture getPicture(
			@PathVariable("picLink") String picLink) {
		
		Picture picture = null;
		try {
			picture = downloadService.link(picLink);
		} catch (PictureRepositoryException e) {
			// TODO: handle exception
		}
		
		//TODO
		
		return picture;
	}
}
