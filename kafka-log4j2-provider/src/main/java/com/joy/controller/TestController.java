package com.joy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-30 17:36
 */
@RestController
public class TestController {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/test")
    public String test(){
        kafkaTemplate.send("default-topic-hello", "hello world");
        return "ok";
    }

}
