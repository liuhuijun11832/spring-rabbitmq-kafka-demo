package com.joy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;

public class KafkaAdminDemo {

    public static void main(String[] args) {
        AdminClient adminClient = KafkaAdminClient.create(initConfigs());
        CreateTopicsResult topics = adminClient.createTopics(
            Collections.singleton(new NewTopic("default-topic-hello", 1, (short)1)));
        topics.values().forEach((k,v)->{
            try {
                v.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(topics);
    }

    private static Map<String,Object> initConfigs(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return configs;
    }

}
