package marchcat;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import marchcat.pictures.Picture;
import marchcat.storage.LocalStorageRepository;
import marchcat.storage.NewLocalStorage;
import marchcat.storage.exception.StorageException;
import marchcat.util.RandomGen;

/**
 * Test for NewLocalStorage
 */
//@SpringBootTest
public class LocalStorageTests {
	
	static LocalStorageRepository localStorageRepository = mock(LocalStorageRepository.class);
	static NewLocalStorage newLocalStorage;
	static int count = 3;
	static Picture[] pictures = new Picture[count];
	static Map<Integer, Integer> picFolders = new HashMap<>();
	
	@BeforeAll
	static void prepare() {
		try {
			newLocalStorage = new NewLocalStorage(localStorageRepository);
			newLocalStorage.STORAGE_DIRECTORY_PATH = Paths.get("C:\\TEST STORAGE FOLDER");
		} catch (StorageException e) {
			e.printStackTrace();
		}
		
		doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				//           0                1
				//insertFile(picture.getId(), c);
				int pId = invocation.getArgument(0);
				int fInt = invocation.getArgument(1);
				picFolders.put(pId, fInt);
				System.out.println("Putting in picFolders: pId = " + pId + " fInt = " + fInt);
				return null;
			}
		}).when(localStorageRepository).insertFile(Mockito.anyInt(), Mockito.anyInt());

		for(int i = 0; i < pictures.length; i++) {
			pictures[i] = new Picture();
			pictures[i].setId(i);
			pictures[i].setName("TESTIMAGE");
			pictures[i].setRnd_name(RandomGen.randomString(20));
			pictures[i].setExt("png");
		}
	}
	
	@Test
	public void testStore() {
		for(int i = 0; i < count; i++) {
			InputStream is = LocalStorageTests.class.getResourceAsStream("/static/images/TESTIMAGE.png");
			
			try {
				assertTrue(newLocalStorage.store(pictures[i], is));
				
			}catch (StorageException e) {
				System.out.println("Failed to TEST the store(): ");
				e.printStackTrace();
			}finally {
				try {
					is.close();
					System.out.println("IS closed");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testDelete() {
		
		for(int i = 0; i < count; i++) {
			
			when(localStorageRepository.getFolder(i)).thenReturn(Integer.toString(picFolders.get(i)));
			
			try {
				newLocalStorage.delete(pictures[i]);
				final int d = i;
				ThrowingRunnable runLoad = () -> newLocalStorage.load(pictures[d]);
				
				assertThrows(StorageException.class, runLoad);
			} catch (StorageException e) {
				e.printStackTrace();
			}
		}
	}
}
