package marchcat.pictures;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Long> {
	//JPA
	//@Query(value = "SELECT p FROM pictures p WHERE ext = :ext")
	
	@Modifying
	@Query("INSERT INTO pictures")
	void insertPicture(Picture picture);
	
	@Query("SELECT * FROM pictures WHERE id = :id")
	Picture finPictureById(int id);
	
	@Query("SELECT * FROM pictures WHERE ext=:ext")
	List<Picture> findPicturesByExt(String ext);
	
}
