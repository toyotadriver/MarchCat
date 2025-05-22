package marchcat.storage;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


public interface LocalStorageRepository extends CrudRepository<StorageEntity, Integer> {

	@Query("INSERT INTO localstorage(id, folder) VALUES (:fileId, :folder)")
	public void insertFile(int fileId, String folder);
}
