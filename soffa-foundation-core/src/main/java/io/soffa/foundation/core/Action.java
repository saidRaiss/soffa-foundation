package io.soffa.foundation.core;

/**
 *
 * @param <I>
 * @param <O>
 */
public interface Action<I, O> {

     O handle(I request, RequestContext context);

}
