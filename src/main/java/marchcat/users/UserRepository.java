package marchcat.users;


import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	//JPA
	//@Query("SELECT u FROM TESTusers u WHERE id=:id")
	
	@Modifying
	@Query("INSERT INTO \"TESTusers\"(username, password) VALUES(:username, :password)")
	boolean insertUser(String username, String password);
	
	@Query("SELECT * FROM \"TESTusers\" WHERE username = :username AND password = :password")
	User findUserByNameAndPassword(String username, String password);
	
	@Query("SELECT * FROM \"TESTusers\" WHERE username = :username")
	User findUserByName(String username);
	
	@Query("SELECT username FROM \"TESTusers\" WHERE id=:id")
	User findUserById(Integer id);
	
}
