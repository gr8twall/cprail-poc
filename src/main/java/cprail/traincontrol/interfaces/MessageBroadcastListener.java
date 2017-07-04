package cprail.traincontrol.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;


@Component
public class MessageBroadcastListener implements ApplicationListener<ContextRefreshedEvent> {

	
    private final MessageSendingOperations<String> messagingTemplate;
    
    @Autowired
    public MessageBroadcastListener(MessageSendingOperations<String> messagingTemplate) {
    	this.messagingTemplate = messagingTemplate;
    }
    
    
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		
		
		messagingTemplate.convertAndSend("/topic/postmessage", "Alert!" );
		
		
	}

}




