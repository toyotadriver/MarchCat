package marchcat.pictures;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, Integer> {

	@Modifying
	@Query("INSERT INTO links(id, link) VALUES(:id, :link)")
	void insertLink(int id, String link);
	
	//@Query("SELECT * FROM links WHERE link = :link")
	Link getLinkByLink(String link);
	
	@Query("SELECT * FROM links WHERE id = :id")
	Link getLinkById(int id);
	
}
