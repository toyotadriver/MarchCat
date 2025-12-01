//package marchcat;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.junit.function.ThrowingRunnable;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import marchcat.pictures.DownloadService;
//import marchcat.pictures.Link;
//import marchcat.pictures.LinkRepository;
//import marchcat.pictures.Picture;
//import marchcat.pictures.PictureRepository;
//import marchcat.pictures.exception.PictureRepositoryException;
//import marchcat.storage.Storage;
//import marchcat.storage.exception.StorageException;
//import marchcat.util.RandomGen;
//
//public class DownloadServiceTest {
//	
//	static PictureRepository pictureRepository = mock(PictureRepository.class);
//	static LinkRepository linkRepository = mock(LinkRepository.class);
//	static Storage storage = mock(Storage.class);
//	static DownloadService downloadService = new DownloadService(pictureRepository, linkRepository, storage);
//	
//	static Picture pic = new Picture();
//	static Link link = new Link();
//	
//	static int picId = 255;
//	static String picName = "TESTIMG";
//	static String picRnd_name = RandomGen.randomString(20);
//	static String picExt = "png";
//	static String linkStr = RandomGen.randomString(20);
//	
//	static byte[] picBytes;
//	
//	@BeforeAll
//	public static void prepare() {
//		
//		pic.setId(picId);
//		pic.setName(picName);
//		pic.setRnd_name(picRnd_name);
//		pic.setExt(picExt);
//		
//		link.setId(picId);
//		link.setLink(linkStr);
//		
//		try (InputStream realIS = DownloadServiceTest.class.getResourceAsStream("/static/images/TESTIMAGE.png");) {
//			picBytes = realIS.readAllBytes();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		InputStream is = mock(InputStream.class);
//		
//		when(linkRepository.getLinkByLink(linkStr)).thenReturn(link);
//		when(pictureRepository.findPictureById(picId)).thenReturn(pic);
//		
//		try {
//			when(is.readAllBytes()).thenReturn(picBytes);
//			
//			when(storage.load(pic)).thenReturn(is);
//		} catch (StorageException | IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void linkPositive() {
//		assertTrue(downloadService.link(linkStr) == pic);
//	}
//	
//	@Test
//	public void linkNegative() {
//		ThrowingRunnable linkNeg = () -> downloadService.link("ABOBA");
//		assertThrows(PictureRepositoryException.class, linkNeg);
//		
//		ThrowingRunnable linkNull = () -> downloadService.link(null);
//		assertThrows(PictureRepositoryException.class, linkNull);
//	}
//	
//	@Test
//	public void getISOfPicturePositive() {
//		assertTrue(downloadService.getBytesOfPicture(pic) == picBytes);
//	}
//
//}
