package marchcat.storage;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;

public interface LocalStorageRepository extends StorageRepository {

	@Override
	@Modifying
	@Query("INSERT INTO localstorage(id, folder) VALUES (:fileid, :folder)")
	public void insertFile(int fileid, int folder);
}
