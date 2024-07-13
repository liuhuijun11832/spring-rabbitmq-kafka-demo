package com.joy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-22 11:23
 */
@Component
public class MyService {

    private static final Logger log = LoggerFactory.getLogger(MyService.class);

    @KafkaListener(id = "default", topics = "default-topic")
    public void process(ConsumerRecord<?, ?> records) {
        log.info("{}===>{}", records.topic(), records.value());
    }

     @KafkaListener(id = "default-transaction-topic", topics = "transaction-topic")
     public void transTopic(ConsumerRecord<?, ?> records) {
         log.info("{}===>{}", records.topic(), records.value());
     }

}
