package marchcat.users;

import org.springframework.stereotype.Service;

import marchcat.pictures.Picture;
import marchcat.pictures.PictureRepository;

@Service
public class AccountService {
	
	PictureRepository pictureRepository;
	
	public AccountService(
			PictureRepository pictureRepository) {
		this.pictureRepository = pictureRepository;
	}
	
	public Picture[] getAccountPictures(int userId) {
		
		Picture[] pics = pictureRepository.findPicturesByAccount(userId);
		
		return pics;
		
	}

}
