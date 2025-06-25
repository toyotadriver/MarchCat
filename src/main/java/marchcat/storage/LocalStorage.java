package marchcat.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import marchcat.storage.exception.StorageException;

@Component("0")
public class LocalStorage implements Storage{
	
	private int storageId = 0;
	
	private String rootDirectory = "C:\\";
	private String folderPrefix = "";
	private String currentPathString;
	private final LocalStorageRepository localStorageRepository;
	private int maxFiles = 10;
	
	public int counter;
	
	private int currentFolderInt = 1;
	//TODO need to get this from table
	private int currentFolderFiles;
	
	
	public LocalStorage(LocalStorageRepository localStorageRepository) throws StorageException {
		this.localStorageRepository = localStorageRepository;
		
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
	public synchronized int store(InputStream is, String name) throws StorageException{
		try {
			if(currentFolderFiles >= maxFiles) {
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
		
		return currentFolderInt;
		
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
	public InputStream load(String fullName) throws StorageException{
		
		Path path = Paths.get(rootDirectory + currentFolderInt + File.separator + fullName);
		try {
			InputStream ins = Files.newInputStream(path);
			
			if(ins == null) {
				throw new StorageException("InputStream is null!");
			}
			
			return ins;
		} catch (IOException e) {
			
			throw new StorageException("Failed to get inputStream from file: " + e.getMessage());
		}
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
	
	@Override
	public StorageRepository getStorageRepository() {
		return localStorageRepository;
	}
	
}
