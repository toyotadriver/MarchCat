package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import marchcat.pictures.exception.PictureValidateException;
import marchcat.pictures.exception.UploadException;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;
import marchcat.users.User;
import marchcat.util.HashGen;
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
	private final RedisTemplate<String, String> redisTemplate;

	public UploadService(Storage storage, PictureRepository pictureRepository, LinkRepository linkRepository, RedisTemplate<String, String> redisTemplate) {
		this.storage = storage;
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
		this.redisTemplate = redisTemplate;
	}
	
	public UploadInitResponse processUploadInitResponse(String userName) {
		String randomTempToken = RandomGen.randomString(32);
		String tokenHash = HashGen.generateStringHash(randomTempToken);
		String uploadId = UUID.randomUUID().toString();
		
		//uploadId is for Key, tokenHash is value
		redisTemplate.opsForHash().put(uploadId, "hash", tokenHash);
		
		redisTemplate.expire(uploadId, 30, TimeUnit.SECONDS);
		
		UploadInitResponse uploadInitResponse = new UploadInitResponse(uploadId, randomTempToken);
		
		return uploadInitResponse;
	}

	/**
	 * Put file to DB and return the Token
	 * @param file
	 * @param user
	 * @return String - storage token
	 * @throws UploadException
	 */
	@Transactional
	public String process(MultipartFile file, int userId) throws UploadException {
		
		//TODO at first, wait for file to upload

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

			//Hashing now
			String hashName = HashGen.generateISHash(is);
			System.out.println("Random filename: " + hashName + '.' + splittedName[1]);

			Boolean valid = false;
			
			Picture pic;
			try {
				valid = validatePicture(splittedName, fileSize);
			} catch (PictureValidateException e) {
				System.out.println(e.getMessage());
				// TO BE LOGGED
			}
			if (valid) {

				int storageId = storage.getStorageId();
				pic = pictureRepository.insertPicture(filename, hashName, splittedName[1], storageId);

				try {
					storage.store(pic, is);
					System.out.println("File stored!");
				} catch (StorageException e) {
					throw new UploadException("File cannot be stored");
				}
				

				//XXX
				Link link = cyclicallyInsertLink(pic.getId());
				
				pic.setLink(link.getLink());

				pictureRepository.insertPictureIntoAccount(pic.getId(), userId);

			} else {
				throw new UploadException("Pic isn't valid");
			}
			
			String uploadToken = RandomGen.randomString(32);
			putToRedis(RequestType.POST + Integer.toString(userId), pic, uploadToken);
			return uploadToken;

		} else {
			throw new UploadException("The file is null");
		}
	}
	
	private Link cyclicallyInsertLink(int id) {
		while(true) {
			String randomLink = RandomGen.randomString(20);
			
			Optional<Link> link = linkRepository.insertLink(id, randomLink);
			if(link.isPresent()) {
				return link.get();
			}
		}
	}

	private void putToRedis(String userIdKeyString, Picture picture, String tempToken) {
		String key = userIdKeyString;
		redisTemplate.opsForHash().put(key, picture.getLink(), picture.getHashName());
		redisTemplate.expire(key, 30, TimeUnit.SECONDS);
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
