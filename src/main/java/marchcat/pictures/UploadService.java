package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import marchcat.pictures.exception.UploadException;
import marchcat.storage.Storage;

@Service
@RequestScope
public class UploadService {

	private final Storage storage;
	private final PictureRepository pictureRepository;

	public UploadService(Storage storage, PictureRepository pictureRepository) {
		this.storage = storage;
		this.pictureRepository = pictureRepository;
	}

	public boolean process(MultipartFile file) {

		InputStream is;
		if (!file.isEmpty()) {
			try {

				is = file.getInputStream();

			} catch (IOException e) {
				throw new UploadException("Failed to get file's inputstream", e);
			}
			
			Picture picture = new Picture();
			String filename = file.getOriginalFilename();
			
			System.out.println("original filename: " + filename);

			//storage.store(is, filename);

		} else {
			throw new UploadException("The file is null");
		}
		
		

		return true;
	}
}
