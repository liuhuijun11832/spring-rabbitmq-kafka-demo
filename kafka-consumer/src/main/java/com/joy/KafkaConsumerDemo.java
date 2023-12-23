package com.joy;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerDemo {

    public static void main(String[] args) {
        Map<String, Object> configs = initConfigs();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs, new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Collections.singleton("default-topic-hello"));
        while (true) {
            try {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.value());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static Map<String, Object> initConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "1000");
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        return configs;
    }

}
