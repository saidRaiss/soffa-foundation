package io.soffa.foundation.spring.config;

import io.soffa.foundation.annotations.DispatchEvent;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.events.EventDispatcher;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
//@ConditionalOnBean(EventDispatcher.class)
public class EventDispatcherAspect {

    private final EventDispatcher eventDispatcher;
    public static final Logger LOG = Logger.get(EventDispatcherAspect.class);

    public EventDispatcherAspect(@Autowired(required = false) EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @SneakyThrows
    @Around("@annotation(event)")
    public Object checkAuthenticated(ProceedingJoinPoint pjp, DispatchEvent event) {
        Object result = pjp.proceed(pjp.getArgs());
        String eventId = event.value();
        eventDispatcher.broadcast(new Event(eventId, result));
        LOG.info("Event dispatched: %s", eventId);
        return result;
    }


}
