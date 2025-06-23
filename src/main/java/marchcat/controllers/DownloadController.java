package marchcat.controllers;

import org.springframework.http.ResponseEntity;
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

	//TODO method to get the Picture and output information
//	@GetMapping("/link/{picLink}")
//	public Picture getPicture(
//			@PathVariable String picLink) {
//		
//		Picture picture = null;
//		try {
//			picture = downloadService.link(picLink);
//		} catch (PictureRepositoryException e) {
//			// TODO: handle exception
//		}
//		
//		//TODO
//		
//		return picture;
//	}
	
	@GetMapping("/link/{picLink}")
	public ResponseEntity<byte[]> getPicture(
			@PathVariable String picLink){
		
		byte[] pictureBytes = null;
		try {
			Picture picture = downloadService.link(picLink);
			String filename = picture.getRnd_name();
			String ext = picture.getExt();
			
			pictureBytes = downloadService.getBytesOfPicture(filename, ext);
		} catch (PictureRepositoryException e) {
			//TODO handle
		}
		
		
		
		return ResponseEntity
			.status(200)
			.body(pictureBytes);
	}
	
}
