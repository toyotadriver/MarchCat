package marchcat.configuration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {
	public static final String EVENT_EXCHANGE = "file.event.exchange";
	public static final String EVENT_QUEUE = "mc.file.event.queue";
	public static final String UPLOADED_EVENT_KEY = "file.uploaded";
	//public static final String DELETED_EVENT_KEY = "file.deleted";
	
	public static final String COMMAND_EXCHANGE = "file.command.exchange";
	public static final String DELETE_COMMAND_KEY = "file.delete";
  

  @Bean
  public TopicExchange eventExchange() {
      return new TopicExchange(EVENT_EXCHANGE, true, false);
  }
  
  @Bean
  public TopicExchange commandExchange() {
  	return new TopicExchange(COMMAND_EXCHANGE, true, false);
  }

  @Bean
  public Queue fileEventQueue() {
      return QueueBuilder
      		.durable(EVENT_QUEUE)
      		.build();
  }

  @Bean
  public Binding eventBinding() {
      return BindingBuilder
          .bind(fileEventQueue())
          .to(eventExchange())
          .with("file.*");
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
      return new Jackson2JsonMessageConverter();
  }
}
