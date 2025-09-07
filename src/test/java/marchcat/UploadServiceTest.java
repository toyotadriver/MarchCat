package marchcat;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import marchcat.pictures.LinkRepository;
import marchcat.pictures.Picture;
import marchcat.pictures.PictureRepository;
import marchcat.pictures.UploadService;
import marchcat.storage.NewLocalStorage;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

@SpringBootTest
public class UploadServiceTest {

	static Storage storage = mock(NewLocalStorage.class);
	static PictureRepository pictureRepository = mock(PictureRepository.class);
	static LinkRepository linkRepository = mock(LinkRepository.class);

	static UploadService uploadService = new UploadService(storage, pictureRepository, linkRepository);
	
	static MockedStatic<RandomGen> mockedRandomGen = mockStatic(RandomGen.class);

	static MockMultipartFile mockMultipartFile;
	static Picture picture = new Picture();
	static int userId = 909;

	@BeforeAll
	public static void prepare() {

		picture.setId(707);
		picture.setName("testFile");
		picture.setExt("png");
		String randomName = "ABOBATWENTYSTRING";
		picture.setRnd_name(randomName);
		int storageId = 5;
		picture.setStorage(storageId);

		InputStream is = UploadServiceTest.class.getResourceAsStream("/static/images/TESTIMAGE.png");
		try {
			mockMultipartFile = new MockMultipartFile(picture.getRnd_name(), picture.getName() + '.' + picture.getExt(), "image/png", is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			when(storage.store(picture, is)).thenReturn(true);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		
		when(pictureRepository.insertPicture(picture.getName() + '.' + picture.getExt(),
				picture.getRnd_name(), picture.getExt(), picture.getStorage()))
		.thenReturn(picture);
		when(pictureRepository.insertPictureIntoAccount(picture.getId(), userId))
		.thenReturn(true);
		when(storage.getStorageId())
		.thenReturn(storageId);
		mockedRandomGen.when(() -> RandomGen.randomString(20)).thenReturn(randomName);
	}

	@Test
	@DisplayName("Positive process() test")
	public void positveProcess() {

		assertTrue(uploadService.process(mockMultipartFile, userId));
		
	}
}
