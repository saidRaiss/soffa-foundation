package io.soffa.foundation.actions;

import io.soffa.foundation.core.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @param <I>
 * @param <O>
 */
public interface Action<I, O> {

    O handle(@NotNull I input, @NotNull RequestContext context);

}
