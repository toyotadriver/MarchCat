package marchcat.storage;

import java.io.InputStream;

import org.springframework.core.io.Resource;

import marchcat.storage.exception.StorageException;

public interface Storage {
	
	/**
	 * Initialize the storage
	 */
	public void init() throws StorageException;
	/**
	 * Store the file with the given name and return folder
	 * @param is
	 * @param name
	 * @return int
	 * @throws StorageException
	 */
	public int store(InputStream is, String name) throws StorageException;
	
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
	public InputStream load(String name) throws StorageException;
	/**
	 * Get storage ID
	 * @return
	 */
	public int getStorageId();
	
	/**
	 * Get storage Repository
	 * @return
	 */
	public StorageRepository getStorageRepository();
}
