package marchcat.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import marchcat.pictures.Picture;
import marchcat.storage.exception.StorageException;

@Component("0")
public class NewLocalStorage implements Storage {

	final LocalStorageRepository storageRepository;

	String disk = "C:\\";
	public String STORAGE_DIRECTORY = disk + "MARCHCAT STORAGE";
	public Path STORAGE_DIRECTORY_PATH = Paths.get(STORAGE_DIRECTORY);
	public List<Path> storageFoldersList;

	public int MAX_FILES = 30;

	public NewLocalStorage(LocalStorageRepository localStorageRepository) throws StorageException {
		this.storageRepository = localStorageRepository;
		try {
			init();
		} catch (StorageException e) {
			throw new StorageException("Failed to init storage: ", e);
		}
	}

	@Override
	public void init() throws StorageException {
		if (!Files.exists(STORAGE_DIRECTORY_PATH)) {
			try {

				Files.createDirectory(STORAGE_DIRECTORY_PATH);
				Files.createDirectory(makePathFromStringArray(new String[] { STORAGE_DIRECTORY, "0" }));

			} catch (IOException e) {
				throw new StorageException("Failed to create storage directory", e);
			}

		}
		try {
			Stream<Path> folderPaths = Files.list(STORAGE_DIRECTORY_PATH);
			Iterator<Path> fPIter = folderPaths.iterator();

			storageFoldersList = new ArrayList<>();
			while (fPIter.hasNext()) {
				Path fpnext = fPIter.next();
				storageFoldersList.add(fpnext);
				System.out.println("Adding to storageFoldersList: " + fpnext.toString());
				System.out.println("init() storageFolderslist size: " + storageFoldersList.size());
			}
			// folderPaths.forEach(s -> {storageFoldersList.add(s);
			// System.out.println("Adding to storageFoldersList: " + s.toString());});

			folderPaths.close();
		} catch (IOException e) {
			throw new StorageException("Failed to list Storage folders ", e);
		}

	}

	private Path makePathFromStringArray(String[] pathStrings) {
		String pathString = pathStrings[0];
		for (int i = 1; i < pathStrings.length; i++) {
			pathString = pathString + File.separator + pathStrings[i];
		}
		return Paths.get(pathString);
	}

	@Transactional
	@Override
	public boolean store(Picture picture, InputStream is) throws StorageException {

		int size = storageFoldersList.size();
		System.out.println("Storage Folder List size: " + size);
		storageFoldersList.forEach(f -> System.out.println("Storage Folder List item: " + f.toString()));
		Path chosen = makePathFromStringArray(new String[] { STORAGE_DIRECTORY, Integer.toString(1) });
		int c = 0;
		try {
			int i = 0;
			while (i <= size) {

				Path cur = storageFoldersList.get(i);
				if (Files.list(cur).count() < MAX_FILES) {
					chosen = cur;
					c = i;
					System.out.println("store() while first if ok : " + cur.toString());
					break;
				} else {
					int s = size - 1;
					if (i == s) {
						String fi = Integer.toString((i + 1));
						System.out.println("Creating folder: " + i);
						Path newFolder = Files.createDirectory(makePathFromStringArray(new String[] { STORAGE_DIRECTORY, fi }));
						storageFoldersList.add(newFolder);
						size++;
					}
				}

				i++;
			}
			
			storageRepository.insertFile(picture.getId(), c);
			
			Path fileDes = Paths.get(chosen.toString(), "/", picture.getRnd_name() + "\\." + picture.getExt());
			System.out.println("Copying: " + fileDes.toString());
			Files.copy(is, fileDes);

			return true;

		} catch (IOException e) {
			throw new StorageException("Failed to store the file: " + chosen.toString(), e);
		}
	}

	@Override
	public void delete(Picture picture) throws StorageException {
		if (storageFoldersList == null) {
			init();
		}
		String folder = storageRepository.getFolder(picture.getId());
		String[] pathString = { STORAGE_DIRECTORY, folder, picture.getRnd_name() + picture.getExt() };

		Path filePath = makePathFromStringArray(pathString);
		try {
			Files.delete(filePath);
		} catch (IOException e) {
			throw new StorageException("Error deleting file", e);
		}
	}

	@Override
	public InputStream load(Picture picture) throws StorageException {

		String folder = storageRepository.getFolder(picture.getId());
		
		try(InputStream ins = Files.newInputStream(Paths.get(STORAGE_DIRECTORY, File.separator, folder))) {
			return ins;
		} catch (IOException e) {
			throw new StorageException("Failed to load IS of the file", e);
		}
		
	}

	@Override
	public int getStorageId() {

		return 0;
	}

	@Override
	public StorageRepository getStorageRepository() {
		return storageRepository;
	}

}
