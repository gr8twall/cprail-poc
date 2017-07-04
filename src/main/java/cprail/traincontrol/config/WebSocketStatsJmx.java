package cprail.traincontrol.config;

public interface WebSocketStatsJmx {

	
	public String getWebSocketSessionStatsInfo();
	
	public String getStompSubProtocolStatsInfo();
	
	public String getStompBrokerRelayStatsInfo();
	
	public String getClientInboundExecutorStatsInfo();
	
	public String getClientOutboundExecutorStatsInfo();
	
	public String getSockJsTaskSchedulerStatsInfo();
	
	
}
