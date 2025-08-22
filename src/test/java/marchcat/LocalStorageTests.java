package marchcat;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import marchcat.pictures.Picture;
import marchcat.storage.LocalStorageRepository;
import marchcat.storage.NewLocalStorage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

/**
 * Test for LocalStorage
 */
//@SpringBootTest
public class LocalStorageTests {
	
	LocalStorageRepository localStorageRepository;
	NewLocalStorage storage;
	
	int times = 70;
	Map<Picture, Path> picsWithPaths = new HashMap<>();
	List<Path> expectedPaths = new ArrayList<Path>();
	String[] fileNames;
	Picture[] testPictures;
	InputStream testPicIS = LocalStorageTests.class.getResourceAsStream("/static/images/TESTIMAGE.png");
	
	LocalStorageTests(){ //@Autowired NewLocalStorage storage
		localStorageRepository = mock(LocalStorageRepository.class);
		//this.storage = storage;
		try {
			this.storage = new NewLocalStorage(localStorageRepository);
		} catch (StorageException e) {
			System.out.println("Failed to construct NewLocaLStporage: " + e.getMessage());
		}
		
	}
	

	//@BeforeAll
	public void prepareExpected() {
		//Switching between Path and String is such a pain
		List<Path> foldersList = new ArrayList<>();
		System.out.println(storage.MAX_FILES);
		System.out.println(storage.STORAGE_DIRECTORY_PATH);
		try(Stream<Path> fPs = Files.list(storage.STORAGE_DIRECTORY_PATH);) {
			
			fPs.forEach(p -> foldersList.add(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int remainingFiles = times;
		
		//Fill
		testPictures = new Picture[times];
		for(int i = 0; i < times; i++) {
			testPictures[i] = new Picture();
			testPictures[i].setRnd_name(RandomGen.randomString(10));
			testPictures[i].setExt("png");
			testPictures[i].setId(i);
		}
		
		for(Path f : foldersList) {
			try {
				if(remainingFiles > 0) {
					remainingFiles -= storage.MAX_FILES - Files.list(f).count();
				} else {
					break;
				}
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(remainingFiles > 0) {
			int newFoldersInt = remainingFiles / storage.MAX_FILES;
			for(int i = 0; i < newFoldersInt + 1; i++) {
				foldersList.add(Paths.get(storage.STORAGE_DIRECTORY, 
						File.separator, 
						Integer.toString(foldersList.size() + i)));
			}
		}
		
		int fni = 0;
		for(Path p : foldersList) {
			expectedPaths.add(Paths.get(p.toString(), File.separator, 
					testPictures[fni].getRnd_name(), 
					".", 
					testPictures[fni].getExt()));
			fni++;
		}
	}
	
	@Test
	@DisplayName("Testing store() and delete() 50 times")
	public void testStoring() {
		prepareExpected();
		
		for(int i = 0; i < times; i++) {
			System.out.println("Storing " + i);
			
			try {
				int picFolderInt = storage.store(testPicIS, testPictures[i].getRnd_name() + "." + testPictures[i].getExt());
				
				picsWithPaths.put(testPictures[i], Paths.get(storage.STORAGE_DIRECTORY_PATH.toString(), 
						File.separator, 
						Integer.toString(picFolderInt)));
				
			} catch (StorageException e) {
				System.out.println("Cause: " + e.getCause());
				e.printStackTrace();
			}
		}
		
		picsWithPaths.forEach((pict, path) -> assertTrue(Files.exists(path)));
		assertTrue(picsWithPaths.size() == testPictures.length);
		for(int i = 0; i < picsWithPaths.size(); i++) {
			assertTrue(Files.exists(picsWithPaths.get(testPictures[i])));
		}
		
	}
}
