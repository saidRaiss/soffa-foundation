package io.soffa.foundation.spring.config;

import io.soffa.foundation.commons.ErrorUtil;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.context.RequestContextHolder;
import io.soffa.foundation.exceptions.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = Logger.get(CustomRestExceptionHandler.class);
    private final Environment environment;

    @Autowired
    public CustomRestExceptionHandler(Environment environment) {
        super();
        this.environment = environment;
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return handleGlobalErrors(ex);
    }

    @ExceptionHandler({UndeclaredThrowableException.class, Throwable.class, Exception.class, TechnicalException.class, FunctionalException.class})
    public ResponseEntity<Object> handleGlobalErrors(Throwable ex) {
        boolean isProduction = environment.acceptsProfiles(Profiles.of("prod", "production"));
        Throwable error = ErrorUtil.unwrap(ex);
        HttpStatus status = deriveStatus(error);
        String message = ErrorUtil.loookupOriginalMessage(error);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("source", environment.getRequiredProperty("spring.application.name"));
        body.put("kind", error.getClass().getSimpleName());
        body.put("status", status.value());
        body.put("message", message);
        body.put("prod", isProduction);

        RequestContextHolder.get().ifPresent(context -> {
            body.put("traceId", context.getTraceId());
            body.put("spanId", context.getSpanId());
            Optional.ofNullable(context.getApplicationName()).ifPresent(s -> body.put("application", s));
            context.getUsername().ifPresent(s -> body.put("user", s));
            if (context.hasTenant()) {
                body.put("tenant", context.getTenantId().getValue());
            }
        });

        if (!(error instanceof FunctionalException) && !(error instanceof FakeException)) {
            LOG.error(error);
        }
        if (!isProduction && status != HttpStatus.UNAUTHORIZED && status != HttpStatus.FORBIDDEN) {
            body.put("trace", ErrorUtil.getStacktrace(error).split("\n"));
        }

        return ResponseEntity.status(status).body(body);
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private HttpStatus deriveStatus(Throwable exception) {
        if (exception instanceof ValidationException || exception instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        } else if (exception instanceof ConflictException) {
            return HttpStatus.CONFLICT;
        } else if (exception instanceof ResponseStatusException) {
            return ((ResponseStatusException) exception).getStatus();
        } else if (exception instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } else if (exception instanceof UnauthorizedException || exception instanceof AccessDeniedException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (exception instanceof NoContentException) {
            return HttpStatus.NO_CONTENT;
        } else if (exception instanceof TodoException) {
            return HttpStatus.NOT_IMPLEMENTED;
        } else if (exception instanceof FunctionalException) {
            return HttpStatus.BAD_REQUEST;
        } else if (exception instanceof SocketException || exception instanceof TimeoutException) {
            return HttpStatus.REQUEST_TIMEOUT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


}
