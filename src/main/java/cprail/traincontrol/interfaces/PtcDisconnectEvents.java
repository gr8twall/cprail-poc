package cprail.traincontrol.interfaces;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class PtcDisconnectEvents implements ApplicationListener<SessionDisconnectEvent> {
	
	private final Log logger = LogFactory.getLog(PtcDisconnectEvents.class);
	
	private final String PTC_USER = "ptc-user";
	
	@Autowired
	SessionMessageHandler msgHandler;
	StompHeaderAccessor stompHeaderAccessor;
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {

		SessionDisconnectEvent se = (SessionDisconnectEvent)event;
		Message<byte[]> msg = se.getMessage();
		stompHeaderAccessor = StompHeaderAccessor.wrap(msg);
		String user = null;
		if(null != stompHeaderAccessor.getNativeHeader(PTC_USER)) {
			user = stompHeaderAccessor.getNativeHeader(PTC_USER).get(0);
		}
		logger.info("DIS-CONNECT [" + stompHeaderAccessor.getSessionId() + "] :" + user);
		
	}

}
