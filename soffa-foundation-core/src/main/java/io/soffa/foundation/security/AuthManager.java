package io.soffa.foundation.security;

import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.model.Authentication;

import java.util.Optional;

public interface AuthManager {

    Optional<Authentication> authenticate(RequestContext context, String token);

    Optional<Authentication> authenticate(RequestContext context, String username, String password);

}
