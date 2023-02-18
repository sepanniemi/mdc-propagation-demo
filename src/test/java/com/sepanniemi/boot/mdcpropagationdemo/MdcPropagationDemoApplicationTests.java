package com.sepanniemi.boot.mdcpropagationdemo;

import io.micrometer.context.ContextRegistry;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MdcPropagationDemoApplicationTests {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void initRestAssured(){
		RestAssured.port = port;
	}

	@Test
	void whenGetCars_thenResponseOkAndMDCLoggedProperly() {
		RestAssured.given()
				.basePath("/cars")
				.header("X-Request-Id", UUID.randomUUID().toString().substring(10))
				.get()
				.then()
				.statusCode(HttpStatus.OK.value());
	}
}
