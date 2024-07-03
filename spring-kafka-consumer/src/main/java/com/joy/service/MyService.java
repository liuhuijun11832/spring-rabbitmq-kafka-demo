package com.joy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
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
@Slf4j
public class MyService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(id = "default", topics = "default-topic")
    public void process(ConsumerRecord<?, ?> records) {
        log.info(records.topic() + "===>" + records.value());
        kafkaTemplate.executeInTransaction(operations -> {
            ProducerRecord<String, String> rec = new ProducerRecord<String, String>("default-topic", "hello", "hello world");
            operations.send(rec);
            return Boolean.TRUE;
        });
    }

    // @KafkaListener(id = "default-log-topic", topics = "log-topic")
    // public void logTopic(ConsumerRecord<?, ?> records) {
    //     log.info(records.topic() + "===>" + records.value());
    // }

}
