package marchcat.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import marchcat.storage.exception.StorageException;

@Component
public class LocalStorage implements Storage{
	
	private Path rootDirectory;
	private String folder = "UP01";
	
	public LocalStorage() {
		this.rootDirectory = Paths.get("C:\\", "\\" + folder);
		
		if(!Files.exists(rootDirectory)) {
			try {
				Files.createDirectories(rootDirectory);
			} catch(IOException e) {
				throw new StorageException("Failed to create dirs", e);
			}
			
			
		}
	}

	@Override
	public void init() throws StorageException{
		
		
	}

	@Override
	public void store(InputStream is) throws StorageException{
		
		
	}

	@Override
	public void delete(Path path) throws StorageException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Resource load(Path path) throws StorageException{
		// TODO Auto-generated method stub
		return null;
	}
	
}
