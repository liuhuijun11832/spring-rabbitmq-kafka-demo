package com.joy.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-22 11:23
 */
@Component
public class MyService {

    @KafkaListener(id = "default",topics = "default-topic")
    public void process(ConsumerRecord<?,?> records){
        System.out.println(records.topic()+"===>"+records.value());
    }

    @KafkaListener(id = "log",topics = "log-topic")
    public void process1(ConsumerRecord<?,?> records){
        System.out.println(records.topic()+"===>"+records.value());
    }

}
