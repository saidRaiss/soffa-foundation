package io.soffa.foundation.actions;

import io.soffa.foundation.core.RequestContext;

/**
 *
 * @param <O>
 */
public interface Action0<O> {

     O handle(RequestContext context);

}
