package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.HttpHandlerDecoratorFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Configuration
public class MdcPropagationConfig {

    Logger log = LoggerFactory.getLogger(MdcPropagationConfig.class);

    public MdcPropagationConfig() {
        Hooks.enableAutomaticContextPropagation();
        ContextRegistry.getInstance().registerThreadLocalAccessor(new ReactorRequestContextMdcBridge());
        log.info("MDC Propagation configured.");
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public HttpHandlerDecoratorFactory mdcReactorContextDecorator() {
        return delegate ->
                (request, response) ->
                        initMdc(delegate, request, response);
    }

    private Mono<Void> initMdc(HttpHandler delegate, ServerHttpRequest request, ServerHttpResponse response) {
        String requestId = request.getHeaders().getFirst("X-Request-ID");
        String userAgent = request.getHeaders().getFirst("User-Agent");
        RequestContext requestContext = new RequestContext(requestId, userAgent);
        return delegate
                .handle(request, response)
                .contextWrite(createReactorRequestContext(requestContext));
    }

    private Context createReactorRequestContext(RequestContext requestContext) {
        log.debug("Create reactor context for the current request context={}", requestContext);
        return Context.of(RequestContext.KEY, requestContext);
    }
}
