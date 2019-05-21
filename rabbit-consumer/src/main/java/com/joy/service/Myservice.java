package com.joy.service;

import com.joy.entity.User;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-20 13:04
 */
@Component
public class Myservice {

    @Value("${default-queue}")
    private String direct;

    /**
     * 监听默认队列
     * @param data
     */
    @RabbitListener(queues = "${default-queue}")
    public void processDefault(String data){
        System.out.println("default-queue"+"===>"+data);
    }

    /**
     * 监听第一个队列
     * @param data
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue1",durable = "true"),
            exchange = @Exchange(value = "joy.fanout.exchange",type = ExchangeTypes.FANOUT,durable = "true"),key = "queue-1"))
    public void process1(String data){
        System.out.println("fanout-queue1"+"===>"+data);
    }

    /**
     * 监听第二个队列
     * @param data
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue2",durable = "true"),
            exchange = @Exchange(value = "joy.fanout.exchange",type = ExchangeTypes.FANOUT,durable = "true"),key = "queue-2"))
    public void process2(String data){
        System.out.println("fanout-queue2"+"===>"+data);
    }

    /**
     * 监听第三个队列
     * @param user
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue3",durable = "true"),
            exchange = @Exchange(value = "joy.direct.exchange",durable = "true"), key = "queue-3"))
    public void process3( User user){
        System.out.println("direct-queue3"+"===>"+user.toString());
    }

    /**
     * 监听第四个队列
     * @param user
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue4",durable = "true"),
            exchange = @Exchange(value = "joy.direct.exchange",durable = "true"),key = "queue-4"))
    public void process4( User user) {
        System.out.println("direct-queue4===>"+user.toString());
    }

    /**
     * 以下为topic交换机测试
     * @param user
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue5",durable = "true"),
            exchange = @Exchange(value = "joy.topic.exchange",type = ExchangeTypes.TOPIC,durable = "true"),key = "51.#"))
    public void process5(User user){
        System.out.println("51.#===>"+user.toString());
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue6",durable = "true"),
            exchange = @Exchange(value = "joy.topic.exchange",type = ExchangeTypes.TOPIC,durable = "true"),key = "*.WEB.#"))
    public void process6(User user){
        System.out.println("*.WEB.#===>"+user.toString());
    }

    /**
     * 监听死信队列消息
     * @param user
     */
    @RabbitListener(queues = "dead-queue")
    public void process7(User user){
        System.out.println("dead-queue===>"+user.toString());
    }

}
