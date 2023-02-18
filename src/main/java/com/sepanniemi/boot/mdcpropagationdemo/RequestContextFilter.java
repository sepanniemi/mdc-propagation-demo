package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class RequestContextFilter implements WebFilter {

    Logger log = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Hooks.enableAutomaticContextPropagation();
        ContextRegistry.getInstance().registerThreadLocalAccessor(new MdcAccessor());
        log.debug("Setting up MDC with header values...");
        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getHeaders().getFirst("X-Request-ID");
        String userAgent = request.getHeaders().getFirst("User-Agent");
        MDC.put("requestId", requestId);
        MDC.put("userAgent", userAgent);
        log.debug("MDC values set.");
        return chain.filter(exchange).contextCapture();
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
