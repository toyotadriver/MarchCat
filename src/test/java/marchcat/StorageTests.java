package marchcat;

import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import marchcat.storage.LocalStorage;
import marchcat.storage.LocalStorageRepository;
import marchcat.storage.Storage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

@SpringBootTest
public class StorageTests {

	InputStream testImage;
	Storage storage;
	LocalStorageRepository localStorageRepository;
	List<String> filesList = new ArrayList<String>();
	
	public StorageTests() {
		try {
			this.storage = new LocalStorage(localStorageRepository);
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void prepare() {
		testImage = this.getClass().getClassLoader().getResourceAsStream("/images/TESTIMAGE.png");

		localStorageRepository = mock(LocalStorageRepository.class);
		

	}

	@Test
	@DisplayName("Testing Storage store() method 10 times")
	public void testStoring() {
		String fileName;
		try {
			for (int i = 0; i < 10; i++) {
				fileName = RandomGen.randomString(10) + ".png";
				filesList.add(fileName);
				storage.store(testImage, fileName);
			}
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void deleteFiles() {
		//TODO
	}
}
