package marchcat.storage;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.core.io.Resource;

import marchcat.storage.exception.StorageException;

public interface Storage {
	
	/**
	 * Initialize the storage
	 */
	public void init() throws StorageException;
	/**
	 * Store the file.
	 * @return boolean success
	 */
	public void store(InputStream is) throws StorageException;
	
	/**
	 * Delete the file
	 * @param path
	 */
	public void delete(Path path) throws StorageException;
	
	/**
	 * Load file as resource
	 * @param path
	 * @return Resource
	 */
	public Resource load(Path path) throws StorageException;
}
