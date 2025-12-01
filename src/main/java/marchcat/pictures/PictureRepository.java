package marchcat.pictures;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Integer> {
	
	//@Modifiyng was deleted because of RETURNING id
	@Query("INSERT INTO pictures(name, hashName, ext, storage) VALUES(:name, :hashName, :ext, :storage) RETURNING *")
	Picture insertPicture(String name, String hashName, String ext, int storage);
	
	//IDK
//	@Modifying
//	Picture save(Picture picture);
	
	//@Query("SELECT * FROM pictures WHERE id = :id")
	//TODO FIND LINK IN THE BASE ON INTERSECTION WITH pictures, beacuse id is foreign key in links
	@Query("SELECT * FROM pictures p JOIN links l ON p.id = l.id WHERE p.id=:id")
	Picture findPictureById(int id);
	
	@Query("SELECT * FROM pictures p "
			+ "JOIN accountPictures a ON p.id = a.picture_id "
			+ "JOIN links l ON p.id = l.id "
			+ "WHERE a.user_id=:userId ORDER BY dou DESC")
	Picture[] findPicturesByAccount(int userId);
	
	@Query("SELECT * FROM pictures p "
			+ "JOIN accountPictures a ON p.id = a.picture_id "
			+ "WHERE p.id=:picId AND a.user_id=:accId")
	Picture findPictureFromAccount(int accId, int picId);
	
	@Query("SELECT * FROM pictures WHERE ext=:ext")
	List<Picture> findPicturesByExt(String ext);
	
	@Query("SELECT currval(pg_get_serial_sequence('pictures', 'id'))")
	int selectCurrId();
	
	@Modifying
	@Query("INSERT INTO accountPictures(user_id, picture_id) VALUES(:userId, :pictureId)")
	boolean insertPictureIntoAccount(int pictureId, int userId);
	
	@Modifying
	@Query("DELETE FROM pictures WHERE id=:picId")
	void deletePictureById(int picId);
}
