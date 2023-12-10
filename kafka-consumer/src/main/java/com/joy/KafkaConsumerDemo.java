package com.joy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class KafkaConsumerDemo {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerDemo.class, args);
    }

}
