package com.joy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

//@EnableConfigurationProperties
//@EnableKafka
@SpringBootApplication
public class SpringKafkaProducerDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaProducerDemo.class, args);
    }

}
