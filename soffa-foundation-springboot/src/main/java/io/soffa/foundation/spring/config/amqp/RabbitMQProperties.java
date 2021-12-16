package io.soffa.foundation.spring.config.amqp;

import lombok.Data;

import java.util.Map;

@Data
public class RabbitMQProperties {

    private Map<String, String> clients;
}
