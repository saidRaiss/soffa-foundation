package io.soffa.foundation.spring.config.amqp;

import com.google.common.collect.ImmutableMap;
import io.soffa.foundation.commons.IdGenerator;
import io.soffa.foundation.commons.JsonUtil;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.pubsub.PubSubClient;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private static final String DLQ = ".dlq";
    private static final String TOPIC = ".topic";
    private static final String FANOUT = ".fanout";
    private static final Logger LOG = Logger.get(RabbitMQConfig.class);
    static boolean embeddedMode;
    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${app.amqp.exchange:app}")
    private String exchange;
    @Value("${app.amqp.routing:services}")
    private String routing;

    public RabbitMQConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    Queue queue() {
        Map<String, Object> args = ImmutableMap.of(
            "x-dead-letter-exchange", exchange + DLQ,
            "x-dead-letter-routing-key", routing + "." + applicationName
        );

        return new Queue(applicationName, true, false, false, args);
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(applicationName + DLQ);
    }

    @Bean
    TopicExchange createTopicExchange() {
        return new TopicExchange(exchange + TOPIC, true, false);
    }

    @Bean
    TopicExchange createDeadLetterExchange() {
        return new TopicExchange(exchange + DLQ, true, false);
    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange(exchange + FANOUT, true, false);
    }

    @Bean
    Binding createTopicBinding() {
        return BindingBuilder.bind(queue()).to(createTopicExchange()).with(routing + "." + applicationName);
    }

    @Bean
    Binding createDQLBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(createDeadLetterExchange()).with(routing + "." + applicationName);
    }

    @Bean
    Binding createFanoutBinding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Primary
    @Bean
    PubSubClient createPubSubClient() {
        return new PubSubClient() {

            @Override
            public void send(String target, Event event) {
                if (TextUtil.isEmpty(event.getId())) {
                    event.setId(IdGenerator.secureRandomId("evt_"));
                }
                rabbitTemplate.convertAndSend(exchange + TOPIC, routing + "." + target, JsonUtil.serialize(event).getBytes());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[PUB-SUB] Message sent to %s - @action:%s", target, event.getAction());
                }
            }

            @Override
            public void broadcast(Event event) {
                rabbitTemplate.convertAndSend(exchange + FANOUT, routing + ".*", JsonUtil.serialize(event).getBytes());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[PUB-SUB] Message broadcasted to %s.* - @action:%s", routing, event.getAction());
                }
            }

            @Override
            public void sendInternal(Event event) {
                send(applicationName, event);
            }
        };
    }


}
