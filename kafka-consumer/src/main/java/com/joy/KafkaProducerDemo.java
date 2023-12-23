package com.joy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Map<String, Object> configs = initConfigs();
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("default-topic-hello", "hello kafka");
        Future<RecordMetadata> future = producer.send(producerRecord);
        RecordMetadata recordMetadata = future.get();
        System.out.println(recordMetadata.offset());
        System.out.println(recordMetadata.timestamp());
    }

    private static Map<String,Object> initConfigs(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return configs;
    }

}
