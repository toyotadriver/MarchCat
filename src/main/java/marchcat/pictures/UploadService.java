package marchcat.pictures;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import marchcat.pictures.exception.UploadException;
import marchcat.storage.Storage;

@Service
@RequestScope
public class UploadService {
	
	private final Storage storage;
	
	public UploadService(Storage storage) {
		this.storage = storage;
	}
	
	public boolean process(MultipartFile file) {
		
		InputStream is;
		if(!file.isEmpty()) {
			try {
				is = file.getInputStream();
				
				
				String filename = file.getOriginalFilename();
				System.out.println("original filename: " + filename);
				
				
				storage.store(is);
				
			} catch (IOException e) {
				throw new UploadException("Failed to get file's inputstream", e);
			}
			
		} else {
			throw new UploadException("The file is null");
		}
		
		return true;
	}
}
