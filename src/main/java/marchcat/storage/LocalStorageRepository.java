package marchcat.storage;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;

/**
 * Should be PROTECTED and available only to storage, but whis will need refactoring
 */
public interface LocalStorageRepository extends StorageRepository {

	@Override
	@Modifying
	@Query("INSERT INTO localstorage(id, folder) VALUES (:fileid, :folder)")
	public void insertFile(int fileid, int folder);
	
	@Query("SELECT folder FROM localstorage WHERE id=:fileId")
	public String getFolder(int fileId);
}
