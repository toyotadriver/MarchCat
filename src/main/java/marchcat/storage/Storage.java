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
	 * Store the file with given name.
	 * @param is
	 * @param name
	 * @throws StorageException
	 */
	public void store(InputStream is, String name) throws StorageException;
	
	/**
	 * Delete the file.
	 * @param name
	 */
	public void delete(String name) throws StorageException;
	
	/**
	 * Load file as resource.
	 * @param path
	 * @return Resource
	 */
	public Resource load(String name) throws StorageException;
	/**
	 * Get storage ID
	 * @return
	 */
	public int getStorageId();
}
