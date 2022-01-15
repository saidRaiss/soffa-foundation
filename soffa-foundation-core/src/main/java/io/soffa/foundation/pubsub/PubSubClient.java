package io.soffa.foundation.pubsub;


import io.soffa.foundation.events.Event;
import io.soffa.foundation.events.EventDispatcher;

public interface PubSubClient extends EventDispatcher {

    void send(String client, String exchange, String routingKey, Event event);

    void send(String channel, Event event);

    Object exchange(Event event);

    <T> T exchange(Event event, Class<T> kind);

    void sendInternal(Event event);

}
