package com.joy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-20 11:28
 */
@EnableRabbit
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

    //@Autowired
    //private MessageListener defaultListener;

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

    /**
     * 使用类作为监听器
     * @return
     */
    /*@Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("default-queue");
        container.setMessageListener(defaultListener);
        return container;
    }*/

    /**
     * 使用方法作为监听器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue defultQueue(){
        return new Queue("default-queue");
    }

    //声明一个正常队列，并添加定时时间和死信交换器路由
    @Bean
    public Queue normalQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        args.put("x-dead-letter-exchange", "joy.dead.direct.exchange");
        args.put("x-dead-letter-routing-key", "dead-queue");
        Queue normalQueue = new Queue("normal-queue",true,false,true,args);
        return normalQueue;
    }

    //声明一个死信队列
    @Bean
    public Queue deadQueue(){return new Queue("dead-queue",true);}

    //声明一个正常交换机
    @Bean
    public Exchange normalExchange(){
        return ExchangeBuilder.directExchange("joy.normal.direct.exchange").durable(true).build();
    }

    //声明一个私心交换机
    @Bean
    public Exchange deadExchange(){
        return ExchangeBuilder.directExchange("joy.dead.direct.exchange").durable(true).build();
    }

    //声明一个正常绑定关系
    @Bean
    public Binding normalBinding(){
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with("normal-queue").noargs();
    }

    //将死信队列和死信交换机绑定上
    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead-queue").noargs();
    }

    //声明一个正常队列1,绑定死信交换机和队列参数
    @Bean
    public Queue normalQueue1(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "joy.dead.direct.exchange");
        args.put("x-dead-letter-routing-key", "dead-queue");
        Queue normalQueue = new Queue("normal-queue1",true,false,true,args);
        return normalQueue;
    }

    //声明一个正常绑定关系
    @Bean
    public Binding normalBinding1(){
        return BindingBuilder.bind(normalQueue1()).to(normalExchange()).with("normal-queue1").noargs();
    }

}
