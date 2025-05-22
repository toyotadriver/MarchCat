package marchcat.pictures;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Integer> {
	
	@Modifying
	@Query("INSERT INTO pictures(name, rnd_name, ext) VALUES(:name, :rnd_name, :ext)")
	void insertPicture(Picture picture);
	
	@Query("SELECT * FROM pictures WHERE id = :id")
	//TODO FIND LINK IN THE BASE ON INTERSECTION WITH pictures, beacuse id is foreign key in links
	Picture findPictureById(int id);
	
	@Query("SELECT * FROM pictures WHERE ext=:ext")
	List<Picture> findPicturesByExt(String ext);
	
}
