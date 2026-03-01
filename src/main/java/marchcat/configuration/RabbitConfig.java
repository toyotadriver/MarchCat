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
	public static final String EXCHANGE = "file.exchange";
  public static final String QUEUE = "mc.file.queue";
  //public static final String ROUTING_KEY = "file.uploaded";

  @Bean
  public TopicExchange fileExchange() {
      return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  public Queue fileUploadedQueue() {
      return QueueBuilder
      		.durable(QUEUE)
      		.build();
  }

  @Bean
  public Binding binding() {
      return BindingBuilder
          .bind(fileUploadedQueue())
          .to(fileExchange())
          .with("file.*");
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
      return new Jackson2JsonMessageConverter();
  }
}
