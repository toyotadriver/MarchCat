package marchcat.messaging;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import marchcat.configuration.RabbitConfig;
import marchcat.pictures.DataService;
import marchcat.util.LoggingAspect;

@Component
public class FileEventListener {
	private final DataService dataService;

  public FileEventListener(DataService dataService) {
      this.dataService = dataService;
  }

//  @RabbitListener(queues = RabbitConfig.QUEUE)
//  public void handleFileEvent(StoredFileRecord record) {
//
//      System.out.println("Received event: " + record);
//
//      //dataService.saveFileMetadata(record);
//  }
  
  @RabbitListener(
      bindings = @QueueBinding(
          value = @Queue(value = RabbitConfig.EVENT_QUEUE, durable = "true"),
          exchange = @Exchange(value = RabbitConfig.EVENT_EXCHANGE, type = "topic"), 
          key = RabbitConfig.UPLOADED_EVENT_KEY))
  public void handleUploaded(StoredFileRecord record) {
  	LoggingAspect.log("Received event: " + record);
  	
  	dataService.saveToDB(record);
  	
  	LoggingAspect.log("Data saved to DB!");
  }
}
