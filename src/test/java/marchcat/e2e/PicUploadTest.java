package marchcat.e2e;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import marchcat.users.Logged;
import marchcat.util.RandomGen;

@SpringBootTest
public class PicUploadTest {

	WebApplicationContext webApplicationContext;
	
	PicUploadTest(WebApplicationContext webApplicationContext){
		this.webApplicationContext = webApplicationContext;
	}
	
	static MockMultipartFile testFile;
	static Logged logged = mock(Logged.class);
	
	@BeforeAll
	public static void prepare() {
		try {
			InputStream is = PicUploadTest.class.getResourceAsStream("/static/images/TESTIMAGEsmall.png");
			byte[] ISBytes = is.readAllBytes();
			is.close();
			
			//Name (0 param) must be the same as in the UPLOAD FORM!
			testFile = new MockMultipartFile("file", "TestFile.png", MediaType.MULTIPART_FORM_DATA.toString(), ISBytes);
			is.close();
		} catch (IOException  e) {
			e.printStackTrace();
			return;
		}
		
		when(logged.getUsername()).thenReturn("tralala");
	}
	
	@Test
	public void uploadPositive() {
		
	}
	
	@Test
	public void uploadNegative() {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		try {
			mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
					.file(testFile))
					.andExpect(redirectedUrl("/main"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
