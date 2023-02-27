package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ReactorRequestContextMdcBridge implements ThreadLocalAccessor<RequestContext> {
    @Override
    public Object key() {
        return RequestContext.KEY;
    }

    @Override
    public RequestContext getValue() {
        return new RequestContext(MDC.get("requestId"), MDC.get("userAgent"));
    }

    @Override
    public void setValue(RequestContext currentContext) {
        MDC.put("requestId", currentContext.requestId());
        MDC.put("userAgent", currentContext.userAgent());
    }

    @Override
    public void reset() {
        MDC.clear();
    }
}
