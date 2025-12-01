package marchcat.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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
	public String storageFolderName = "MARCHCAT STORAGE";
	public String STORAGE_DIRECTORY;
	public Path STORAGE_DIRECTORY_PATH;
	public List<Path> storageFoldersList;

	public int MAX_FILES = 30;

	public NewLocalStorage(LocalStorageRepository localStorageRepository) throws StorageException {
		this.storageRepository = localStorageRepository;
		String osName = System.getProperty("os.name");
		System.out.println(osName + "LMAO");
		if(osName.equals("Linux")) {
			
			//HAVE TO MOUNT VOLUME
			this.disk = "/var/lib/MarchCat/";
		}
		
		STORAGE_DIRECTORY = disk + storageFolderName;
		STORAGE_DIRECTORY_PATH = Paths.get(STORAGE_DIRECTORY);
		
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
			//To sort in normal order, not 1, 10, 11, 12, 2, 3, 4, 5, ...
			Comparator<Path> pathComparator = (a, b) -> {
				int first = Integer.parseInt(a.getFileName().toString());
				int second = Integer.parseInt(b.getFileName().toString());
				return (first - second);
			};
			
			Stream<Path> folderPaths = Files.list(STORAGE_DIRECTORY_PATH).sorted(pathComparator);
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

			Path fileDes = Paths.get(chosen.toString(), "/", picture.getHashName() + '.' + picture.getExt());
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
		String[] pathString = { STORAGE_DIRECTORY, folder, picture.getHashName() + '.' + picture.getExt() };

		Path filePath = makePathFromStringArray(pathString);
		try {
			Files.delete(filePath);
		} catch (IOException e) {
			throw new StorageException("Error deleting file", e);
		}
	}

	@Override
	public InputStream load(Picture picture) throws StorageException {

		String folder = getPictureFolder(picture);
		String filefullName = picture.getHashName() + '.' + picture.getExt();

		Path filePath = Paths.get(STORAGE_DIRECTORY, File.separator, folder, File.separator, filefullName);
		if (Files.exists(filePath)) {
			try {
				InputStream ins = Files
						.newInputStream(Paths.get(STORAGE_DIRECTORY, File.separator, folder, File.separator, filefullName));
				return ins;
			} catch (IOException e) {
				throw new StorageException("Failed to load IS of the file", e);
			}
		} else {
			throw new StorageException("The file not found");
		}

	}
	
	public String getPictureFolder(Picture picture) {
		return storageRepository.getFolder(picture.getId());
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
