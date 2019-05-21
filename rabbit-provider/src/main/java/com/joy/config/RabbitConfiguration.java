package com.joy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: rabbitmq配置
 * @Author: Joy
 * @Date: 2019-05-17 17:27
 */
@Configuration
public class RabbitConfiguration {

    @Value("${rabbit.hostname}")
    private String rabbitHost;
    @Value("${rabbit.port}")
    private int rabbitPort;
    @Value("${rabbit.username}")
    private String rabbitUName;
    @Value("${rabbit.password}")
    private String rabbitPassword;

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitHost);
        cachingConnectionFactory.setPort(rabbitPort);
        cachingConnectionFactory.setUsername(rabbitUName);
        cachingConnectionFactory.setPassword(rabbitPassword);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(){
        return new RabbitAdmin((connectionFactory()));
    }

    @Bean
    public AmqpTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    //这个是默认队列，没有声明任何绑定关系，会绑定到默认的交换机
    @Bean
    public Queue defaultQueue(){
        Queue queue = new Queue("default-queue",true);
        return queue;
    }

    //声明一个可以持久化的fanout交换机
    @Bean
    public Exchange testFanoutExchange(){
        return ExchangeBuilder.fanoutExchange("joy.fanout.exchange").durable(true).build();
    }
    //声明第一个队列
    @Bean
    public Queue testFanoutQueue1(){
        Queue queue = new Queue("queue1",true);
        return queue;
    }
    //将第一个队列绑定到fanout交换机上
    @Bean
    public Binding testFanoutBuilding(){
        return BindingBuilder.bind(testFanoutQueue1()).to(testFanoutExchange()).with("queue-1").noargs();
    }

    //声明第二个队列
    @Bean
    public Queue testFanoutQueue2(){
        return new Queue("queue2", true);
    }

    //将第二个队列绑定到fanout交换机上
    @Bean
    public Binding testFanoutBuilding1(){
        return BindingBuilder.bind(testFanoutQueue2()).to(testFanoutExchange()).with("queue-2").noargs();
    }

    //声明第三个队列
    @Bean
    public Queue directQueue(){
        return new Queue("queue3");
    }

    //声明一个direct类型的交换机
    @Bean
    public Exchange testDirectExchange(){
        return ExchangeBuilder.directExchange("joy.direct.exchange").durable(true).build();
    }

    //将第三个队列绑定到direct交换机上
    @Bean
    public Binding testDirectBinding1(){
        return BindingBuilder.bind(directQueue()).to(testDirectExchange()).with("queue-3").noargs();
    }

    //声明第四个队列
    @Bean
    public Queue directQueue1(){
        return new Queue("queue4");
    }

    //将第四个队列绑定感到direct交换机上
    @Bean
    public Binding testDirectBinding2(){
        return BindingBuilder.bind(directQueue1()).to(testDirectExchange()).with("queue-4").noargs();
    }
}
