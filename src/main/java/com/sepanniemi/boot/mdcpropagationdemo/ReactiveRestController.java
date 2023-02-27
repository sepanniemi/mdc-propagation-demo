package com.sepanniemi.boot.mdcpropagationdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

@RestController
public class ReactiveRestController {

    private static Logger log = LoggerFactory.getLogger(ReactiveRestController.class);

    private final CarsRepository carsRepository;

    public ReactiveRestController(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    @GetMapping("/cars")
    public Mono<ResponseEntity<Cars>> getCars() {
        log.debug("GET cars called.");
        return carsRepository.getCars()
                .transformDeferredContextual(
                        (carsMono, contextView) ->
                                contextualResponse(carsMono, contextView)
                );
    }

    private static Mono<ResponseEntity<Cars>> contextualResponse(Mono<Cars> carsMono, ContextView contextView) {
        String requestId =
                contextView.<RequestContext>getOrEmpty(RequestContext.KEY)
                        .map(requestContext -> requestContext.requestId())
                        .orElse("undefined");
        return carsMono.map(c -> ok(requestId, c));
    }

    private static ResponseEntity<Cars> ok(String requestId, Cars cars) {
        log.debug("Returning cars={} for requestId={}", cars, requestId);
        return ResponseEntity
                .ok()
                .header("X-Request-Id", requestId)
                .body(cars);
    }
}
