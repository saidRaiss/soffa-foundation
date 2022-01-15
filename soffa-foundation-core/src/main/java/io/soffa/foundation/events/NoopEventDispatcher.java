package io.soffa.foundation.events;

import io.soffa.foundation.commons.Logger;

public class NoopEventDispatcher implements EventDispatcher {

    private static final Logger LOG = Logger.get(NoopEventDispatcher.class);

    @Override
    public void broadcast(Event event) {
        if (LOG.isInfoEnabled()) {
            LOG.info("[NoopEventDispatcher] dispatch event: " + event.getAction());
        }
    }

}
