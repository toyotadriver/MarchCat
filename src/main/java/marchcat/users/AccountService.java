package marchcat.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import marchcat.pictures.Picture;
import marchcat.pictures.PictureRepository;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;

@Service
public class AccountService {
	
	PictureRepository pictureRepository;
	Storage storage;
	
	public AccountService(
			PictureRepository pictureRepository,
			Storage storage) {
		this.pictureRepository = pictureRepository;
		this.storage = storage;
	}
	
	public Picture[] getAccountPictures(int userId) {
		
		Picture[] pics = pictureRepository.findPicturesByAccount(userId);
		
		return pics;
		
	}
	
	/**
	 * Check if picture is on the account
	 * @param picId
	 * @return
	 */
	public boolean getPictureFromAccount(int accId, int picId) {
		
		return pictureRepository.findPictureFromAccount(accId, picId) != null;
	}
	
	@Transactional
	public void deletePicture(int picId) {
		
		Picture pic = pictureRepository.findPictureById(picId);
		String picFilename = pic.getRnd_name() + "." + pic.getExt();
		
		pictureRepository.deletePictureById(picId);
		try {
			storage.delete(picFilename);
		} catch (StorageException e) {
			// TODO: handle exception
		}
		
		
	}
}
