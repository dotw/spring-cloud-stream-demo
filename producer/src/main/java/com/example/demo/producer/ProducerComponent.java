package com.example.demo.producer;

import com.example.demo.util.Event;
import com.example.demo.util.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Component
public class ProducerComponent {
    @Autowired
    private ObjectMapper jsonMapper;

    private final EmitterProcessor<Message<?>> processor = EmitterProcessor.create();

//    @Autowired
//    private StreamBridge streamBridge;

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/", method = POST, consumes = "*/*")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleRequest(@RequestBody Product body, @RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType) throws Exception {
        Event<String, Product> event = new Event(Event.Type.CREATE,body.getId(), body);
        Message<?> message = MessageBuilder.withPayload(event)
                .setHeader("spring.cloud.stream.sendto.destination", body.getId()).build();
        processor.onNext(message);
    }

    @Bean
    public Supplier<Flux<Message<?>>> supplier() {
        return () -> processor;
    }
}
