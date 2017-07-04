package cprail.traincontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

public class WebSocketStatsJmxImpl implements WebSocketStatsJmx {

	WebSocketMessageBrokerStats webSocketMessageBrokerStats;
	
	public WebSocketStatsJmxImpl() {
        super();
        System.out.println("WebSocketStatsJmxImpl::Constructor");
    }
	
	public WebSocketMessageBrokerStats getWebSocketMessageBrokerStats() {
        return webSocketMessageBrokerStats;
    }
	
	@Autowired
    public void setWebSocketMessageBrokerStats(WebSocketMessageBrokerStats webSocketMessageBrokerStats) {
        this.webSocketMessageBrokerStats = webSocketMessageBrokerStats; 
    }
	
	
	@ManagedAttribute(description="Get stats about WebSocket sessions.")  // defines an attribute of an MBean
    public String getWebSocketSessionStatsInfo(){
        return webSocketMessageBrokerStats.getWebSocketSessionStatsInfo();
    }
    @ManagedAttribute(description="Get stats about STOMP-related WebSocket message processing.")
    public String getStompSubProtocolStatsInfo(){
        return webSocketMessageBrokerStats.getStompSubProtocolStatsInfo();
    }
    @ManagedAttribute(description="Get stats about STOMP broker relay (when using a full-featured STOMP broker).")
    public String getStompBrokerRelayStatsInfo(){
        return webSocketMessageBrokerStats.getStompBrokerRelayStatsInfo();
    }
    @ManagedAttribute(description="Get stats about the executor processing incoming messages from WebSocket clients.")
    public String getClientInboundExecutorStatsInfo(){
        return webSocketMessageBrokerStats.getClientInboundExecutorStatsInfo();
    }
    @ManagedAttribute(description="Get stats about the executor processing outgoing messages to WebSocket clients.")
    public String getClientOutboundExecutorStatsInfo(){
        return webSocketMessageBrokerStats.getClientOutboundExecutorStatsInfo();
    }
    @ManagedAttribute(description="Get stats about the SockJS task scheduler.")
    public String getSockJsTaskSchedulerStatsInfo(){
        return webSocketMessageBrokerStats.getSockJsTaskSchedulerStatsInfo();
    }
	
	
	
	
}
