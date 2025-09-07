package marchcat.e2e;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class PicUploadTest {

	WebApplicationContext webApplicationContext;
	
	PicUploadTest(WebApplicationContext webApplicationContext){
		this.webApplicationContext = webApplicationContext;
	}
	
	@Test
	public void fullUploadTest() {
		MockMultipartFile testFile;
		
		try {
			testFile = new MockMultipartFile("testFile", "TestFile.png", "image/png", PicUploadTest.class.getResourceAsStream("/static/images/TESTIMAGE.png"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		try {
			mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(testFile))
		//.andExpect(content().contentType(MediaType.TEXT_HTML))
		.andExpect(MockMvcResultMatchers.forwardedUrl("/main"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
