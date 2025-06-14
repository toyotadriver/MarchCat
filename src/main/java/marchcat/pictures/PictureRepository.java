package marchcat.pictures;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Integer> {
	
	//@Modifiyng was deleted because of RETURNING id
	@Query("INSERT INTO pictures(name, rnd_name, ext, storage) VALUES(:name, :rnd_name, :ext, :storage) RETURNING id")
	int insertPicture(String name, String rnd_name, String ext, int storage);
	
	//IDK
	@Modifying
	Picture save(Picture picture);
	
	@Query("SELECT * FROM pictures WHERE id = :id")
	//TODO FIND LINK IN THE BASE ON INTERSECTION WITH pictures, beacuse id is foreign key in links
	Picture findPictureById(int id);
	
	@Query("SELECT * FROM pictures WHERE ext=:ext")
	List<Picture> findPicturesByExt(String ext);
	
	@Query("SELECT currval(pg_get_serial_sequence('pictures', 'id'))")
	int selectCurrId();
}
