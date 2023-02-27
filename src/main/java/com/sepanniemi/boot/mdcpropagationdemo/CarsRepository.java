package com.sepanniemi.boot.mdcpropagationdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Component
public class CarsRepository {

    Logger log = LoggerFactory.getLogger(CarsRepository.class);

    Cars cars = new Cars(
            List.of(
                    new Car("Honda", "Civic"),
                    new Car("Toyota", "Corolla"),
                    new Car("Opel", "Kadett")
            )
    );

    public Mono<Cars> getCars() {
        return Mono.fromCallable(() -> cars)
                .delayElement(Duration.ofMillis(200))
                .doOnSuccess(c -> log.debug("Returning cars={}", c))
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel());

    }
}
