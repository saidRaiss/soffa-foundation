package io.soffa.foundation.spring.config.amqp;

import io.soffa.foundation.events.EventDispatcher;
import io.soffa.foundation.events.NoopEventDispatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBeansFactory {

    @ConditionalOnMissingBean(EventDispatcher.class)
    public EventDispatcher createNoopEventDispatcher() {
        return new NoopEventDispatcher();
    }
}
