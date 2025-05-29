package marchcat.storage;

import java.io.File;
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
	
	private int storageId = 0;
	
	private String rootDirectory = "C:\\";
	private String folderPrefix = "";
	private String currentPathString;
	private int maxFiles = 10;
	
	private int currentFolderInt = 1;
	private int currentFolderFiles;
	
	
	public LocalStorage() throws StorageException {
		currentPathString = rootDirectory + "\\" + folderPrefix + currentFolderInt;
		
		Path folderPath = Paths.get(currentPathString);
		if(!Files.exists(folderPath)) {
			try {
				Files.createDirectories(folderPath);
			} catch(IOException e) {
				throw new StorageException("Failed to create dirs", e);
			}
			
			
		}
	}

	@Override
	public void init() throws StorageException{
		
		
	}

	@Override
	public synchronized void store(InputStream is, String name) throws StorageException{
		try {
			if(currentFolderFiles == maxFiles) {
			newFolder();
		}
		} catch(StorageException e) {
			notifyAll();
			throw new StorageException("Failed to store file: " + name, e);
		}
		
		Path filePath = Paths.get(currentPathString + File.separator + name);
		
		try {
			Files.copy(is, filePath);
		} catch(IOException e) {
			throw new StorageException("Failed to write inputStream to file", e);
		}
		
		
		
	}

	@Override
	public void delete(String name) throws StorageException{
		Path filePath = Paths.get(currentPathString + File.separator + name);
		try {
			Files.delete(filePath);
		} catch(IOException e) {
			throw new StorageException("Error deleting file", e);
		}
		
		
	}

	@Override
	public Resource load(String name) throws StorageException{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Creates new folder and increments currentFolder.
	 */
	private void newFolder() throws StorageException {
		Path newFolderPath = Paths.get(rootDirectory, folderPrefix + (currentFolderInt + 1));
		try {
			Files.createDirectory(newFolderPath);
			currentFolderInt++;
		} catch(IOException e) {
			throw new StorageException("Failed to create folder " + folderPrefix + currentFolderInt, e);
		}
		
	}

	@Override
	public int getStorageId() {
		return storageId;
	}
	
	
	
}
