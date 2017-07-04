package cprail.traincontrol.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    ConnectionFactory jmsConnectionFactory() {
        // use a pool for ActiveMQ connections
        PooledConnectionFactory pool = new PooledConnectionFactory();
        pool.setConnectionFactory(new ActiveMQConnectionFactory("tcp://127.0.0.1:61616"));
        return pool;
    }

    @Bean
    RouteBuilder myRouter() {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // listen the queue named track_mq and upon receiving a new entry
                // simply redirect it to a bean named queueHandler which will then send it to users over STOMP
                from("activemq:track_mq").to("bean:trackMqHandler");
            }
        };
    }
}



