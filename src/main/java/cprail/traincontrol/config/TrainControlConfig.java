package cprail.traincontrol.config;

import java.security.Principal;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class TrainControlConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {

	@Autowired
	UserHandshakeHandler handshakeHandler;
	
	

	@Override
	public void configureMessageBroker(MessageBrokerRegistry msgBrokerReg) {
		msgBrokerReg.enableSimpleBroker("/topic");
		msgBrokerReg.setApplicationDestinationPrefixes("/cprail");
//		msgBrokerReg.setApplicationDestinationPrefixes("/cprail")
//			.enableStompBrokerRelay("/topic","/track_mq")
//			.enableStompBrokerRelay("/topic", "/track_mq");
		
//		msgBrokerReg.setApplicationDestinationPrefixes("/cprail")
//		.enableStompBrokerRelay("/track_mq", "/topic");
//		msgBrokerReg.enableStompBrokerRelay("/track_mq", "/topic").setSystemLogin("guest").setSystemPasscode("guest");
//		msgBrokerReg.enableStompBrokerRelay("/track_mq", "/topic").setClientLogin("guest").setClientPasscode("guest`");
//		msgBrokerReg.enableStompBrokerRelay("/track_mq", "/topic").setRelayHost("localhost").setRelayPort(61616);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/cprail-websocket-endpt").withSockJS();
		// registry.addEndpoint("/anotherendpoint");
		// registry.addEndpoint("/managetrack").withSockJS();
		registry.addEndpoint("/managetrack");
		registry.addEndpoint("/managetrack").setHandshakeHandler(handshakeHandler).withSockJS();
		registry.addEndpoint("/implystate");
		registry.addEndpoint("/managechat");
		registry.addEndpoint("/managechat").setHandshakeHandler(handshakeHandler).withSockJS();
		registry.addEndpoint("/chatmessage");
		
		//registry.addEndpoint("/implystate").setHandshakeHandler(handshakeHandler).withSockJS();
		// registry.addEndpoint("/managetrack").addInterceptors(new
		// HttpHandshakeInterceptor());
		// registry.addEndpoint("/broadcast").withSockJS();
		// registry.addEndpoint("/track/state/change").withSockJS();
		// registry.addEndpoint("/track/state/changed").withSockJS();
		// registry.addEndpoint("/track/switch").withSockJS();
		// registry.addEndpoint("/track/switched").withSockJS();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		// registry.addHandler(myHandler(),
		// "/mypath").setAllowedOrigins("http://comingfromhttp.com", "https://comingformsecuredhttp.com" , "sip://ipbasedevice.com");

		// ALL ORIGINS
		// registry.addHandler(myOtherHandler(),
		// "/allpath").setAllowedOrigins("*");
	}

	@Bean
	WebSocketHandler myHandler() {
		return new MyHandler();
	}

	@Bean
	WebSocketHandler myOtherHandler() {
		return new MyOtherHandler();
	}
}

@Component
class UserHandshakeHandler extends DefaultHandshakeHandler {

	private String[] USERS = { "taiwo", "fred", "jason" };

	protected Principal getUser(ServerHttpRequest req, WebSocketHandler wsHandler, Map<String, Object> attrib) {
		String user = getRandom(this.USERS);
		return new UsernamePasswordAuthenticationToken(user, null);
	}

	private String getRandom(String[] users) {
		int rand = getRandomInt(users.length);
		return users[rand];
	}

	private int getRandomInt(int length) {
		return new Random().nextInt(length);
	}

}
