package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import marchcat.pictures.exception.PictureRepositoryException;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

@Service
@RequestScope
public class DownloadService {

	private final PictureRepository pictureRepository;
	private final LinkRepository linkRepository;
	private final Storage storage;

	public DownloadService(
			PictureRepository pictureRepository,
			LinkRepository linkRepository,
			Storage storage) {
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
		this.storage = storage;
	}

	public Picture link(String linkStr) throws PictureRepositoryException {

		Link link = linkRepository.getLinkByLink(linkStr);
		Picture picture;

		if (link != null && validateLink(linkStr)) {

			int id = link.getId();
			picture = pictureRepository.findPictureById(id);
			
			
			return picture;
		} else {
			throw new PictureRepositoryException("The link is null");
		}

	}
	
	public byte[] getBytesOfPicture(String fileName, String ext) throws PictureRepositoryException {
		try {
			
			Resource resource = storage.load(fileName + '.' + ext);
			return resource.getContentAsByteArray();
			
		} catch (StorageException | IOException e) {
			throw new PictureRepositoryException(e.getMessage(), e.getCause());
		}
		
		
	}

	private boolean validateLink(String linkStr) {
		String chars = RandomGen.CHARACTERS;
		boolean actual;

		for (int i = 0; i < linkStr.length(); i++) {
			actual = false;
			for(int j = 0; j < chars.length(); j++) {
				if(linkStr.charAt(i) == chars.charAt(j)) {
					actual = true;
					break;
				}
			}
			if(actual == false) {
				return false;
			}
		}
		return true;
	}
}
