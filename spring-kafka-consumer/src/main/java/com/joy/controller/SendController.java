package com.joy.controller;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * 发送消息
 *
 * @author liuhuijun
 * @since 2024/7/7 18:22
 */
@RestController
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);
    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    private Tracer tracer;

    @PostMapping("/send")
    public String send(@RequestBody String body, @RequestParam("topic") String topic) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<Integer, String>> send = kafkaTemplate.send(topic, body);
        ProducerRecord<Integer, String> producerRecord = send.get().getProducerRecord();
        return producerRecord.value();
    }

}
