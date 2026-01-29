package marchcat.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:marchcat.properties") //Addtitional properties file besides default application.properties
@ComponentScan(basePackages = {"marchcat.users"})
@EnableAspectJAutoProxy
public class ProjectConfiguration {

	@Bean
	public JdbcTemplate jdbct(DataSource dataSource) {
		if(dataSource == null) {
			System.out.println("datasource is null!!!");
		}
		return new JdbcTemplate(dataSource);
	}
	
//	@Bean
//	public JedisConnectionFactory jedisConnectionFactory() {
//		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//		
//		return jedisConnectionFactory;
//	}
	
	public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory factory){
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
		//Autowired factory
		redisTemplate.setConnectionFactory(factory);
		
		return redisTemplate;
	}
}
