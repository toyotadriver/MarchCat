package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import marchcat.pictures.exception.PictureValidateException;
import marchcat.pictures.exception.UploadException;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;

@Service
@RequestScope
public class UploadService {

	private final long MAXSIZE = 3000000L;
	private final String[] AVAILABLEFORMATS = { "jpg", "jpeg", "png", "gif", "bmp" };
	private final String RESTRICTEDFORMAT = "webp";

	private final Storage storage;
	private final PictureRepository pictureRepository;

	public UploadService(Storage storage, PictureRepository pictureRepository) {
		this.storage = storage;
		this.pictureRepository = pictureRepository;
	}

	@Transactional
	public boolean process(MultipartFile file) {

		InputStream is;
		if (!file.isEmpty()) {
			try {

				is = file.getInputStream();

			} catch (IOException e) {
				throw new UploadException("Failed to get the file's inputstream", e);
			}
			
			String filename = file.getOriginalFilename();

			System.out.println("original filename: " + filename);

			Boolean valid = false;
			try {
				valid = validatePicture(file);
			} catch (PictureValidateException e) {
				// TO BE LOGGED
			}
			if (valid) {
				
				try {
					storage.store(is, filename);
				} catch (StorageException e) {
					// TODO: handle exception
					return false;
				}
				
			} else {
				return false;
			}
			
			Picture picture = new Picture();
			
			Boolean written = writePicture(picture);
			
			if(written) {
				return true;
			} else {
				try {
					storage.delete(filename);
				} catch(StorageException e) {
					//TO BE LOGGED
				}
				return false;
			}
			
		} else {
			throw new UploadException("The file is null");
		}
	}
	
	private boolean writePicture(Picture picture){
		
		
		
		return true;
	}

	private boolean validatePicture(MultipartFile file) throws PictureValidateException {

		//Dot needs to be escaped
		for(String str : file.getOriginalFilename().split("\\.")) {
			System.out.println("SOSAL?");
			System.out.println(str);
		}
		
		String ext = file.getOriginalFilename().split("\\.")[1];
		for (String format : AVAILABLEFORMATS) {
			if (ext.equals(format)) {
				if (file.getSize() < MAXSIZE) {
					return true;
				} else {
					throw new PictureValidateException("The picture is too big");
				}
			}
		}
		throw new PictureValidateException("The picture's format is invalid");
	}
}
