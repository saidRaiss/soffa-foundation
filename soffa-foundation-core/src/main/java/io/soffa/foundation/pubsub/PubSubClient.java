package io.soffa.foundation.pubsub;


import io.soffa.foundation.events.Event;

public interface PubSubClient {

    void send(String client, String exchange, String routingKey, Event event);

    void send(String channel, Event event);

    void broadcast(Event event);

    Object exchange(Event event);

    <T> T exchange(Event event, Class<T> kind);

    void sendInternal(Event event);

}
