package cprail.traincontrol.interfaces;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;


@Component
public class PtcConnectedEvents implements ApplicationListener<SessionConnectedEvent> {
	
	private final Log logger = LogFactory.getLog(PtcConnectedEvents.class);
	
	private final String PTC_USER = "ptc-user";
	
	@Autowired
	SessionMessageHandler msgHandler;
	StompHeaderAccessor stompHeaderAccessor;
	
	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {

		SessionConnectedEvent se = (SessionConnectedEvent)event;
		Message<byte[]> msg = se.getMessage();
		stompHeaderAccessor = StompHeaderAccessor.wrap(msg);
		String user = stompHeaderAccessor.getUser().getName();// NativeHeader(PTC_USER).get(0);
		logger.info("CONNECT-ED [" + stompHeaderAccessor.getSessionId() + "] :" + user);
		
	}

}
