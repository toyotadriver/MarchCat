package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;

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

	public DownloadService(PictureRepository pictureRepository, LinkRepository linkRepository, Storage storage) {
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
		this.storage = storage;
	}

	public Picture link(String linkStr) throws PictureRepositoryException {

		Link link = linkRepository.getLinkByLink(linkStr);
		Picture picture;

		if (link != null) {
			if (validateLink(linkStr)) {

				int id = link.getId();
				picture = pictureRepository.findPictureById(id);

				return picture;
			} else {
				throw new PictureRepositoryException("The link is invalid");
			}
		} else {
			throw new PictureRepositoryException("The link is null");
		}

	}

	public byte[] getInputStreamOfPicture(Picture picture) throws PictureRepositoryException {
		
		try(InputStream ins = storage.load(picture);) {
			
			return ins.readAllBytes();

		} catch (StorageException | IOException e) {
			throw new PictureRepositoryException("Pic repo exception" + e.getMessage(), e.getCause());
		}

	}
	
	public String getPictureLinkStringById(int id) {
		
		return linkRepository.getLinkById(id).getLink();
	}

	private boolean validateLink(String linkStr) {
		String chars = RandomGen.CHARACTERS;
		boolean actual;

		for (int i = 0; i < linkStr.length(); i++) {
			actual = false;
			for (int j = 0; j < chars.length(); j++) {
				if (linkStr.charAt(i) == chars.charAt(j)) {
					actual = true;
					break;
				}
			}
			if (actual == false) {
				return false;
			}
		}
		return true;
	}
}
