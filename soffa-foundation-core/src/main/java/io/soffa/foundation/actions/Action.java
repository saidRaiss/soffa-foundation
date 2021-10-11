package io.soffa.foundation.actions;

import io.soffa.foundation.core.RequestContext;

/**
 * @param <I>
 * @param <O>
 */
public interface Action<I, O> {

    O handle(I request, RequestContext context);

}
