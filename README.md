# mdc-propagation-demo

This demo Spring Boot Webflux app shows how to initialize SLF4J MDC with some header values, and how to use new cool Reactor feature to propagate the MDC to new thread scheduled by Reactor.


Below log shows MDC values propagated to parallel scheduler threads:
```
2023-02-22T18:58:29.948+02:00 DEBUG [ userAgent: requestId: ] 30917 --- [ctor-http-nio-2] c.s.b.m.MdcPropagationConfig             : Setting up MDC with header values...
2023-02-22T18:58:29.948+02:00 DEBUG [ userAgent:Apache-HttpClient/4.5.14 (Java/17.0.6) requestId:070-4262-93f8-358316aa92af ] 30917 --- [ctor-http-nio-2] c.s.b.m.MdcPropagationConfig             : MDC values set.
2023-02-22T18:58:29.979+02:00 DEBUG [ userAgent:Apache-HttpClient/4.5.14 (Java/17.0.6) requestId:070-4262-93f8-358316aa92af ] 30917 --- [ctor-http-nio-2] c.s.b.m.ReactiveRestController           : GET cars called.
2023-02-22T18:58:30.011+02:00 DEBUG [ userAgent:Apache-HttpClient/4.5.14 (Java/17.0.6) requestId:070-4262-93f8-358316aa92af ] 30917 --- [     parallel-1] c.s.b.mdcpropagationdemo.CarsRepository  : Returning cars=Cars[cars=[Car[brand=Honda, model=Civic], Car[brand=Toyota, model=Corolla], Car[brand=Opel, model=Kadett]]]
```
