package com.joy.controller;

import com.joy.config.ImageStyleProperties;
import com.joy.config.KafkaStyleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-30 17:36
 */
@Slf4j
@RestController
public class TestController {

//    @Autowired
//    KafkaTemplate<String,String> kafkaTemplate;

    private ImageStyleProperties imageStyleProperties;

    private KafkaStyleProperties kafkaStyleProperties;

    @Autowired
    public TestController(ImageStyleProperties imageStyleProperties, KafkaStyleProperties kafkaStyleProperties) {
        this.imageStyleProperties = imageStyleProperties;
        this.kafkaStyleProperties = kafkaStyleProperties;
    }

    @GetMapping("/test")
    public String test(){
//        kafkaTemplate.send("default-topic-hello", "hello world");
        log.info("imageStyleProperties:{}", imageStyleProperties);
        log.info("kafkaStyleProperties:{}", kafkaStyleProperties);
        return "ok";
    }

}
