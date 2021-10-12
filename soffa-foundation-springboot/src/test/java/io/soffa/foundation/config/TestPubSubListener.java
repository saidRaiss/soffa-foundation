package io.soffa.foundation.config;

import io.soffa.foundation.events.Event;
import io.soffa.foundation.pubsub.PubSubListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class TestPubSubListener implements PubSubListener {

    public static final AtomicInteger TICK = new AtomicInteger(0);
    private static final String HELLO = "HELLO";

    @Override
    public void handle(Event event) {
        if (HELLO.equals(event.getAction())) {
            TICK.incrementAndGet();
        }
    }
}
