package marchcat.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import marchcat.pictures.Picture;
import marchcat.storage.exception.StorageException;

@Component("0")
public class NewLocalStorage implements Storage {

	final LocalStorageRepository storageRepository;

	static String disk = "C:\\";
	static String STORAGE_DIRECTORY = disk + "MARCHCAT STORAGE";
	static Path STORAGE_DIRECTORY_PATH = Paths.get(STORAGE_DIRECTORY);
	static List<Path> storageFoldersList = new ArrayList<>();

	int MAX_FILES = 30;

	public NewLocalStorage(LocalStorageRepository localStorageRepository) {
		this.storageRepository = localStorageRepository;
		try {
			init();
		} catch (StorageException e) {
			System.out.println("Failed to initialize storage " + e.getCause());
		}

	}

	@Override
	public void init() throws StorageException {
		if(!Files.exists(STORAGE_DIRECTORY_PATH)) {
			try {
				
				Files.createDirectory(STORAGE_DIRECTORY_PATH);
				Path newFolder = Files.createDirectory(makePathFromStringArray(new String[]{STORAGE_DIRECTORY, "0"}));
				storageFoldersList.add(newFolder);
				
			} catch (IOException e) {
				throw new StorageException("Failed to create storage directory", e);
			}
			
		}
		try (Stream<Path> folderPaths = Files.list(STORAGE_DIRECTORY_PATH)) {
			folderPaths.forEach(s -> storageFoldersList.add(s));

		} catch (IOException e) {
			throw new StorageException("Failed to list Storage folders ", e);
		}

	}
	
	private Path makePathFromStringArray(String[] pathStrings) {
		String pathString = pathStrings[0];
		for(int i = 1; i < pathStrings.length; i++) {
			pathString = pathString + File.separator + pathStrings[i];
		}
		return Paths.get(pathString);
	}

	@Override
	public int store(InputStream is, String name) throws StorageException {
		int size = storageFoldersList.size();
		Path chosen = makePathFromStringArray(new String[] {STORAGE_DIRECTORY, Integer.toString(1)});
		int c = 0;
		try {
			for (int i = 0; i < size; i++) {
				
				Path cur = storageFoldersList.get(i);
				if(Files.list(cur).count() < MAX_FILES) {
					chosen = cur;
					c = i;
					break;
				}
				if(i + 1 > size) {
					String fi = Integer.toString(i + 1);
					Path newFolder = Files.createDirectory(makePathFromStringArray(new String[]{STORAGE_DIRECTORY, fi}));
					storageFoldersList.add(newFolder);
					chosen = newFolder;
					c = i + 1;
					break;
				}
			}
			
			Path fileDes = Paths.get(chosen.toString(), "/", name);
			Files.copy(is, fileDes);
			return c;
			
		} catch (IOException e) {
			throw new StorageException("Failed to store the file: " + chosen.toString(), e);
		}
	}

	@Override
	public void delete(Picture picture) throws StorageException {
		String folder = storageRepository.getFolder(picture.getId());
		String[] pathString = {STORAGE_DIRECTORY, folder, picture.getRnd_name() + picture.getExt()};
		
		Path filePath = makePathFromStringArray(pathString);
		try {
			Files.delete(filePath);
		} catch (IOException e) {
			throw new StorageException("Error deleting file", e);
		}
	}

	@Override
	public InputStream load(Picture picture) throws StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStorageId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public StorageRepository getStorageRepository() {
		return storageRepository;
	}

}
