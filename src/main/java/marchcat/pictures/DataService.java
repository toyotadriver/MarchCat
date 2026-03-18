package marchcat.pictures;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import marchcat.configuration.RabbitConfig;
import marchcat.messaging.StoredFileRecord;
import marchcat.pictures.exception.DataException;
import marchcat.storage.Storage;
import marchcat.users.UserRepository;
import marchcat.util.HashGen;
import marchcat.util.LoggingAspect;
import marchcat.util.RandomGen;

@Service
//@RequestScope
public class DataService {

	private final long MAXSIZE = 3000000L;
	private final String[] AVAILABLEFORMATS = { "jpg", "jpeg", "png", "gif", "bmp" };
	private final String RESTRICTEDFORMAT = "webp";

	private final Storage storage;
	private final PictureRepository pictureRepository;
	private final LinkRepository linkRepository;
	private final UserRepository userRepository;
	private final RedisTemplate<String, String> redisTemplate;

	public DataService(Storage storage, PictureRepository pictureRepository, LinkRepository linkRepository,
			UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
		this.storage = storage;
		this.pictureRepository = pictureRepository;
		this.linkRepository = linkRepository;
		this.userRepository = userRepository;
		this.redisTemplate = redisTemplate;
	}

	public UploadInitResponse processUploadInitResponse(String userName) {
		String randomTempToken = RandomGen.randomString(32);
		String tokenHash = HashGen.generateStringHash(randomTempToken);
		LoggingAspect.log("GENERATED TOKEN HASH: " + tokenHash);
		String uploadId = UUID.randomUUID().toString();

		// uploadId is for Key, tokenHash is value
		redisTemplate.opsForHash().put(uploadId, "hash", tokenHash);
		redisTemplate.opsForHash().put(uploadId, "user", userName);

		redisTemplate.expire(uploadId, 30, TimeUnit.SECONDS);

		UploadInitResponse uploadInitResponse = new UploadInitResponse(uploadId, randomTempToken);

		return uploadInitResponse;
	}

	/**
	 * Put file to DB and return the Token
	 * 
	 * @param file
	 * @param user
	 * @return String - storage token
	 * @throws DataException
	 */
	@Transactional
	public String saveToDB(StoredFileRecord fileRecord){

		Picture pic;

		int storageId = 0; // For now
		pic = pictureRepository.insertPicture(fileRecord.originalName(), 
				fileRecord.hashName(), 
				fileRecord.extension(),
				storageId);

		// XXX
		Link link = cyclicallyInsertLink(pic.getId());

		pic.setLink(link.getLink());

		int userId = userRepository.findUserByName(
				(String) redisTemplate.opsForHash()
				.get(fileRecord.uploadId(), 
						"user"))
				.getId();

		pictureRepository.insertPictureIntoAccount(pic.getId(), userId);
		
		redisTemplate.expire(fileRecord.uploadId(), Duration.ZERO);
		
		String uploadToken = RandomGen.randomString(32);

		return uploadToken;
	}

	private Link cyclicallyInsertLink(int id) {
		while (true) {
			String randomLink = RandomGen.randomString(20);

			Optional<Link> link = linkRepository.insertLink(id, randomLink);
			if (link.isPresent()) {
				return link.get();
			}
		}
	}

	private void putToRedis(String userIdKeyString, Picture picture, String tempToken) {
		String key = userIdKeyString;
		redisTemplate.opsForHash().put(key, picture.getLink(), picture.getHashName());
		redisTemplate.expire(key, 30, TimeUnit.SECONDS);
	}

	private String[] splitName(String fullname) {
		String[] splitted = fullname.split("\\.");
		return splitted;
	}

}
