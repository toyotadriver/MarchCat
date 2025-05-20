package marchcat.pictures;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.pictures.exception.PictureRepositoryException;

@Service
@RequestScope
public class DownloadService {
	
	private final PictureRepository pictureRepository;
	private final LinkRepository linkRepository;
	
	public DownloadService(
			PictureRepository pictureRepository,
			LinkRepository linkRepository) {
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
	}
	
	public Picture link(String linkStr) throws PictureRepositoryException{
		
		Link link = linkRepository.getLinkByLink(linkStr);
		Picture picture;
		
		if(link != null) {
			
			int id = link.getId();
			picture = pictureRepository.findPictureById(id);
			
			return picture;
		} else {
			throw new PictureRepositoryException("The link is null");
		}
		
	}
	
	private boolean verifyLink(String linkStr) {
		
		//TODO
		
		return true;
	}
}
