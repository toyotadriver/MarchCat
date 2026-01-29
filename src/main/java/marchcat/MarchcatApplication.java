package marchcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = RedisRepositoriesAutoConfiguration.class)
public class MarchcatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarchcatApplication.class, args);
	}

}
