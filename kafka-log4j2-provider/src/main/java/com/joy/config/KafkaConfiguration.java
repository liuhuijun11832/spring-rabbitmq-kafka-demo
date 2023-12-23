package com.joy.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-22 10:11
 */
@Configuration
public class KafkaConfiguration {



    @Bean
    public NewTopic defaultTopic(){
        return new NewTopic("default-topic",1,(short) 1);
    }

    @Bean
    public NewTopic logTopic(){
        return new NewTopic("log-topic", 1, (short) 1);
    }

}
