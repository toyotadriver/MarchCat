package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import marchcat.pictures.exception.PictureValidateException;
import marchcat.pictures.exception.UploadException;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

@Service
@RequestScope
public class UploadService {

	private final long MAXSIZE = 3000000L;
	private final String[] AVAILABLEFORMATS = { "jpg", "jpeg", "png", "gif", "bmp" };
	private final String RESTRICTEDFORMAT = "webp";

	private final Storage storage;
	private final PictureRepository pictureRepository;
	private final LinkRepository linkRepository;

	public UploadService(
			Storage storage,
			PictureRepository pictureRepository,
			LinkRepository linkRepository) {
		this.storage = storage;
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
	}

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

			Long fileSize = file.getSize();
			String[] splittedName = splitName(filename);

			String rndName = RandomGen.randomString(20);
			System.out.println("Random filename: " + rndName + '.' + splittedName[1]);

			Boolean valid = false;
			try {
				valid = validatePicture(splittedName, fileSize);
			} catch (PictureValidateException e) {
				System.out.println(e.getMessage());
				// TO BE LOGGED
			}
			if (valid) {

				try {
					storage.store(is, rndName + '.' + splittedName[1]);
					System.out.println("File stored!");
				} catch (StorageException e) {
					// TODO: handle exception
					System.out.println("File cannot be stored");
					System.out.println(e.getMessage());
					return false;
				}

			} else {
				System.out.println("Pic isn't valid");
				return false;
			}

			int id = pictureRepository.insertPicture(filename, rndName, splittedName[1]);
			linkRepository.insertLink(id, RandomGen.randomString(20));

			return true;

		} else {
			throw new UploadException("The file is null");
		}
	}

	private boolean writePicture(Picture picture) {
		// pictureRepository.insertPicture(picture);

		return true;
	}

	private boolean validatePicture(String[] fullname, Long fileSize) throws PictureValidateException {

		String ext = fullname[1];
		for (String format : AVAILABLEFORMATS) {
			if (ext.equals(format)) {
				if (fileSize < MAXSIZE) {
					return true;
				} else {
					throw new PictureValidateException("The picture is too big");
				}
			}
		}
		throw new PictureValidateException("The picture's format is invalid");
	}

	private String[] splitName(String fullname) {
		String[] splitted = fullname.split("\\.");
		return splitted;
	}

}
