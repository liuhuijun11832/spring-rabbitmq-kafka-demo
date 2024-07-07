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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerDemo.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        sendNormalMsg();
        sendTransMsg();
    }

    private static void sendNormalMsg() throws InterruptedException, ExecutionException {
        Map<String, Object> configs = initConfigs();
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("default-topic", "hello", "hello kafka");
        Future<RecordMetadata> future = producer.send(producerRecord);
        RecordMetadata recordMetadata = future.get();
        logger.info("=====>{}", recordMetadata.offset());
        logger.info("=====>{}", recordMetadata.timestamp());
    }

    private static void sendTransMsg() {
        Map<String, Object> configs = initTransConfigs();
        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);
        producer.initTransactions();
        producer.beginTransaction();
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("transaction-topic", "hello", "hello kafka with transaction");
        ProducerRecord<String,String> producerRecord1 = new ProducerRecord<>("transaction-topic", "hello", "hello kafka with transaction-1");
        try {
            Future<RecordMetadata> sendResult1 = producer.send(producerRecord);
            Future<RecordMetadata> sendResult2 = producer.send(producerRecord1);
            logger.info("=====>{}", sendResult1.get().offset());
            logger.info("=====>{}", sendResult2.get().offset());
            producer.commitTransaction();
        } catch (Exception e) {
            producer.abortTransaction();
        }
    }

    private static Map<String,Object> initConfigs(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return configs;
    }

    private static Map<String,Object> initTransConfigs(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional-id");
        return configs;
    }

}
