package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

import java.util.Map;

@Configuration
public class MdcPropagationConfig {

    Logger log = LoggerFactory.getLogger(MdcPropagationConfig.class);

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public HttpHandlerDecoratorFactory mdcReactorContextDecorator() {
        return delegate ->
                (request, response) ->
                        initMdc(delegate, request, response);
    }

    private Mono<Void> initMdc(HttpHandler delegate, ServerHttpRequest request, ServerHttpResponse response) {
        Hooks.enableAutomaticContextPropagation();
        ContextRegistry.getInstance().registerThreadLocalAccessor(new MdcAccessor());
        log.debug("Setting up MDC with header values...");
        String requestId = request.getHeaders().getFirst("X-Request-ID");
        String userAgent = request.getHeaders().getFirst("User-Agent");
        MDC.put("requestId", requestId);
        MDC.put("userAgent", userAgent);
        log.debug("MDC values set.");
        return delegate
                .handle(request, response)
                .contextCapture();
    }

    static class MdcAccessor implements ThreadLocalAccessor<Map<String, String>> {

        static final String KEY = "mdc";

        @Override
        public Object key() {
            return KEY;
        }

        @Override
        public Map<String, String> getValue() {
            return MDC.getCopyOfContextMap();
        }

        @Override
        public void setValue(Map<String, String> value) {
            MDC.setContextMap(value);
        }

        @Override
        public void reset() {
            MDC.clear();
        }
    }
}
