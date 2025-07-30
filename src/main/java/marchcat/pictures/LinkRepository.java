package marchcat.pictures;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, Integer> {

	@Modifying
	@Query("INSERT INTO links(id, link) VALUES(:id, :link)")
	void insertLink(int id, String link);
	
	/**
	 * Get Link object by link String
	 * @param link
	 * @return Link
	 */
	//@Query("SELECT * FROM links WHERE link = :link")
	Link getLinkByLink(String link);
	
	/**
	 * Get link by Picture Id
	 * @param id
	 * @return Link
	 */
	@Query("SELECT * FROM links WHERE id = :id")
	Link getLinkById(int id);
	
}
