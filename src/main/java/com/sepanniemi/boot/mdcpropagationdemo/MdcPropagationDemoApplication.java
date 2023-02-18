package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ThreadLocalAccessor;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

import java.util.Map;

@SpringBootApplication
public class MdcPropagationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdcPropagationDemoApplication.class, args);
	}
}
