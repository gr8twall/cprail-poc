package cprail.traincontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmergencyBroadcastController {

	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public EmergencyBroadcastController(SimpMessagingTemplate simpMessagingTemplate) {
		super();
		this.simpMessagingTemplate = simpMessagingTemplate;
	}
	
	
//	@RequestMapping( path="/broadcast" )
	public void broadcastMessage() {
		
		this.simpMessagingTemplate.convertAndSend("/topic/broadcast", "Alert!");
		
		
	}
	
}
