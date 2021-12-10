package io.soffa.foundation.client;


import io.soffa.foundation.commons.JsonUtil;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.commons.http.HttpClient;
import io.soffa.foundation.commons.http.HttpRequest;
import io.soffa.foundation.commons.http.HttpResponse;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.exceptions.ForbiddenException;
import io.soffa.foundation.exceptions.FunctionalException;
import io.soffa.foundation.exceptions.TechnicalException;
import io.soffa.foundation.exceptions.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.Path;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class RestClient implements InvocationHandler {

    private final HttpClient client;
    private final String baseUrl;
    private final Map<String, ApiInfo> infos = new HashMap<>();

    private RestClient(HttpClient client, String baseUrl, Class<?> clientInterface) {
        this.client = client;
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        for (Method method : clientInterface.getMethods()) {
            Path path = method.getAnnotation(Path.class);
            Operation operation = method.getAnnotation(Operation.class);
            if (path == null || operation == null) {
                throw new TechnicalException("Method '%s' should be annotated with @Path and @Operation", method.getName());
            }
            if (TextUtil.isEmpty(path.value())) {
                throw new TechnicalException("@Path value is required on methid '%s'", method.getName());
            }
            if (TextUtil.isEmpty(operation.method())) {
                throw new TechnicalException("@Operationd.method is required on methid '%s'", method.getName());
            }
            infos.put(method.getName(), new ApiInfo(operation.method(), "/" + path.value().replaceAll("^/+", "")));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clientInterface, String baseUrl) {
        return newInstance(clientInterface, baseUrl, HttpClient.getInstance());
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clientInterface, String baseUrl, HttpClient client) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{clientInterface},
            new RestClient(client, baseUrl, clientInterface));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        if ("hashCode".equals(method.getName())) {
            return baseUrl.hashCode();
        }
        if ("equals".equals(method.getName())) {
            return method.equals(args[0]);
        }
        HttpResponse response = this.client.request(createRequest(method, args));
        return parseResponse(response, method);
    }

    public Object parseResponse(HttpResponse response, Method method) {
        if (response.is2xxSuccessful()) {
            return JsonUtil.deserialize(response.getBody(), method.getReturnType());
        }
        if (response.isForbidden()) {
            throw new ForbiddenException(response.getMessageOrBody());
        }
        if (response.isUnauthorized()) {
            throw new UnauthorizedException(response.getMessageOrBody());
        }
        if (response.is4xxClientError()) {
            throw new FunctionalException(response.getMessageOrBody());
        }
        throw new TechnicalException(response.getMessageOrBody());
    }

    public HttpRequest createRequest(Method method, Object... args) {
        ApiInfo apiInfo = infos.get(method.getName());
        if (apiInfo == null) {
            throw new TechnicalException("Method not registered: %s", method.getName());
        }

        HttpRequest request = new HttpRequest(apiInfo.getMethod(), baseUrl + apiInfo.getPath());
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof RequestContext) {
                    RequestContext context = (RequestContext) arg;
                    request.setHeaders(context.getHeaders());
                } else {
                    request.setBody(arg);
                }
            }
        }
        return request;
    }


}
