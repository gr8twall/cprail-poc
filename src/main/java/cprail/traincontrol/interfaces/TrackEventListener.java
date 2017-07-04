package cprail.traincontrol.interfaces;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


import cprail.traincontrol.model.Track;

@Component
public class TrackEventListener implements ApplicationListener {
	
	private final Log logger = LogFactory.getLog(TrackEventListener.class);
	
	private final String PTC_USER = "ptc-user";
	
	@Autowired
	SessionMessageHandler msgHandler;
	
//	SessionConnectedEvent sessionConnectedEvent;
//	Message<byte[]> msg;
//	StompHeaderAccessor connectedheaderAccessor;
	StompHeaderAccessor stomPHeaderAccessor;
	
	
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired	
	public TrackEventListener(SimpMessagingTemplate simpMessagingTemplate) {
		
		this.simpMessagingTemplate = simpMessagingTemplate;
		
	}
	

	@Override
	public void onApplicationEvent(ApplicationEvent appEvent) {
		// TODO Auto-generated method stub
		String hook = "";
		
		if(appEvent instanceof BrokerAvailabilityEvent){
			System.out.println("Broker availability!");
		}
		
		if(appEvent instanceof ContextRefreshedEvent) {
			System.out.println("Context Refreshed Event!");
		}
		
		if(appEvent instanceof EmbeddedServletContainerInitializedEvent) {
			System.out.println("EmbeddedServlet Container Initialized Event");
		}

		if(appEvent instanceof ApplicationReadyEvent) {
			System.out.println("Application Ready Event");
		}
		
		if(appEvent instanceof ServletRequestHandledEvent) {
			//System.out.println("Servlet Request Handled Event");
		}
		if(appEvent instanceof SessionConnectEvent) {
			
			SessionConnectEvent se = (SessionConnectEvent)appEvent;
			Message<byte[]> msg = se.getMessage();
			stomPHeaderAccessor = StompHeaderAccessor.wrap(msg);
//			msgHandler.addHeaderAccessor(connectedheaderAccessor);
			String user = stomPHeaderAccessor.getNativeHeader(PTC_USER).get(0);
			
			logger.info("CONNECT [" + stomPHeaderAccessor.getSessionId() + "] :" + user);
			
		}
		
		if(appEvent instanceof SessionConnectedEvent) {
			
			SessionConnectedEvent se = (SessionConnectedEvent)appEvent;
			Message<byte[]> msg = se.getMessage();
			stomPHeaderAccessor = StompHeaderAccessor.wrap(msg);
//			msgHandler.addHeaderAccessor(connectedheaderAccessor);
//			String user = stomPHeaderAccessor.getNativeHeader(PTC_USER).get(0);
//			
//			logger.info("CONNECT-ED [" + stomPHeaderAccessor.getSessionId() + "] :" + user);
			
		}
		
		if(appEvent.getClass().equals(SessionSubscribeEvent.class)) {
			
			
			
			SessionSubscribeEvent sessionSubscribeEvent = (SessionSubscribeEvent)appEvent;
			Message<byte[]> msg = sessionSubscribeEvent.getMessage();
			stomPHeaderAccessor = StompHeaderAccessor.wrap(msg);
			if(null!=stomPHeaderAccessor && null!=stomPHeaderAccessor.getSessionId()) {
				//System.out.println("Event Listener -> " + subscribeheaderAccessor.getSessionId());
			}
		}
		
		if(appEvent.getClass().equals(SessionDisconnectEvent.class)) {
			
			SessionDisconnectEvent se = (SessionDisconnectEvent)appEvent;
			Message<byte[]> msg = se.getMessage();
			stomPHeaderAccessor = StompHeaderAccessor.wrap(msg);
//			String user = stomPHeaderAccessor.getNativeHeader(PTC_USER).get(0);
//			logger.info("DIS-CONNECTED [" + stomPHeaderAccessor.getSessionId() + "] :" + user);
			
		}
		
	}

	
	
	
	
	
}
