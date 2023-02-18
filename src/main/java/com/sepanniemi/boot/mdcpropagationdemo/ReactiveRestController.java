package com.sepanniemi.boot.mdcpropagationdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveRestController {

    Logger log = LoggerFactory.getLogger(ReactiveRestController.class);

    private final CarsRepository carsRepository;

    public ReactiveRestController(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    @GetMapping("/cars")
    public Mono<Cars> getCars(){
        log.debug("GET cars called.");
        return carsRepository.getCars();
    }
}
