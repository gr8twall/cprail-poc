package cprail.traincontrol.interfaces;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


@Component
public class PtcConnectEvents implements ApplicationListener<SessionConnectEvent> {
	
	private final Log logger = LogFactory.getLog(PtcConnectEvents.class);
	
	private final String PTC_USER = "ptc-user";
	
	@Autowired
	SessionMessageHandler msgHandler;
	StompHeaderAccessor stompHeaderAccessor;
	
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {

		SessionConnectEvent se = (SessionConnectEvent)event;
		Message<byte[]> msg = se.getMessage();
		stompHeaderAccessor = StompHeaderAccessor.wrap(msg);
		String user = stompHeaderAccessor.getNativeHeader(PTC_USER).get(0);
		logger.info("CONNECT [" + stompHeaderAccessor.getSessionId() + "] :" + user);
		
		logger.info("Header UUID: " + stompHeaderAccessor.getId());
		
	}

}
