package io.soffa.foundation.actions;

import io.soffa.foundation.core.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @param <O>
 */
public interface Action0<O> {

    O handle(@NotNull RequestContext context);

}
