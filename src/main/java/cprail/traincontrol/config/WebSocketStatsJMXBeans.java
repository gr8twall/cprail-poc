package cprail.traincontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
@EnableMBeanExport
public class WebSocketStatsJMXBeans {
    @Bean
    public WebSocketStatsJmxImpl jmxWebSocketStatsJmxImpl() {
        return new WebSocketStatsJmxImpl();
    }
}