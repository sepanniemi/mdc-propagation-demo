package com.sepanniemi.boot.mdcpropagationdemo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MdcPropagationDemoApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void initRestAssured() {
        RestAssured.port = port;
    }

    @Test
    void whenGetCars_thenResponseOkAndMDCLoggedProperly() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            service.submit(
                    () -> {
                        requestGetCars();
                        latch.countDown();
                    }
            );
        }
		latch.await();
    }

    private static void requestGetCars() {
        RestAssured.given()
                .basePath("/cars")
                .header("X-Request-Id", UUID.randomUUID().toString().substring(10))
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
