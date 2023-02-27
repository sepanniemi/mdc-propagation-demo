package com.sepanniemi.boot.mdcpropagationdemo;

public record RequestContext(String requestId, String userAgent) {
    public static final Class<?> KEY = RequestContext.class;
}
