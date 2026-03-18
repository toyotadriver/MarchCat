package marchcat.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import marchcat.configuration.RabbitConfig;

@Component
public class FileCommandSender {
	private final RabbitTemplate rabbitTemplate;
	
	FileCommandSender(RabbitTemplate rabbitTemplate){
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void commandDeleteFile(FileToDelete fileToDelete) {
		rabbitTemplate.convertAndSend(
				RabbitConfig.COMMAND_EXCHANGE,
				RabbitConfig.DELETE_COMMAND_KEY,
				fileToDelete);
	}
}
