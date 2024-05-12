package com.joy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-22 11:23
 */
@Component
@Slf4j
public class MyService {

    @KafkaListener(id = "default", topics = "default-topic")
    public void process(ConsumerRecord<?, ?> records) {
        log.info(records.topic() + "===>" + records.value());
    }

    @KafkaListener(id = "default-log-topic", topics = "log-topic")
    public void logTopic(ConsumerRecord<?, ?> records) {
        log.info(records.topic() + "===>" + records.value());
    }

}
