package cprail.traincontrol.messaging;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import cprail.traincontrol.model.Track;

@Component( value="trackMqHandler" )
public class QueueHandler {

	
	@Autowired
	private SimpMessageSendingOperations msgTemplate;
	
	
	private static Map<String, Object> defaultHeaders;
	
	
	static {
		defaultHeaders = new HashMap<>();
		defaultHeaders.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
	}
	
	public void handle(Exchange exchange) {
		
		
		Message camelMsg = (Message) exchange.getIn();
		String mqTrackMsg = camelMsg.getBody(String.class);
		
		// TODO add JSON processing
		
		msgTemplate.convertAndSend("/topic/postmessage", mqTrackMsg );
		
	}
	
}












