package com.example.demo.consumer;

import com.example.demo.util.Event;
import com.example.demo.util.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    //Following sink is used as test consumer. It logs the data received through the consumer.
    static class TestSink {

        private final Log logger = LogFactory.getLog(getClass());

        @Bean
        public Consumer<Event<String, Product>> receive1() {
            return data -> {
                logger.info("Data received from customer-1..." + data);
                Product product = data.getData();
                logger.info(" product id is " + product.getId());
            };
        }

        @Bean
        public Consumer<Event<String, Product>> receive2() {
            return data -> {
                logger.info("Data received from customer-2..." + data);
                Product product = data.getData();
                logger.info(" product id is " + product.getId());
            };
        }

        @Bean
        public Function<String, String> handle1() {
            return data -> {
                logger.info(" received from handle1..." + data);
                return "handle1:" + data;
            };
        }

        @Bean
        public Function<String, String> handle2() {
            return data -> {
                logger.info(" received from handle2..." + data);
                return "handle2:" + data;
            };
        }
    }
}
