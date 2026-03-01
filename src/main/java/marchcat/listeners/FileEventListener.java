package marchcat.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import marchcat.configuration.RabbitConfig;
import marchcat.pictures.DataService;
import marchcat.pictures.StoredFileRecord;

@Component
public class FileEventListener {
	private final DataService dataService;

  public FileEventListener(DataService dataService) {
      this.dataService = dataService;
  }

  @RabbitListener(queues = RabbitConfig.QUEUE)
  public void handleFileEvent(StoredFileRecord record) {

      System.out.println("Received event: " + record);

      //dataService.saveFileMetadata(record);
  }
}
