package com.joy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
//@EnableKafka
@SpringBootApplication
public class SpringKafkaConsumerDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaConsumerDemo.class, args);
    }

}
