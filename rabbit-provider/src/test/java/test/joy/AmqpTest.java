package test.joy;

import com.joy.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-20 10:50
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void sendToDefaultQueue(){
        //使用默认交换机
        rabbitTemplate.convertAndSend("default-queue","默认队列");
    }

    @Test
    public void sendToFanoutExchange(){
        //自定义交换机以及与其绑定的对列名
        rabbitTemplate.setExchange("joy.fanout.exchange");
        rabbitTemplate.setRoutingKey("queue-1");
        rabbitTemplate.convertAndSend("我是谁？我在哪？我要干什么？");
        rabbitTemplate.setRoutingKey("queue-3");
        rabbitTemplate.convertAndSend("我是谁？我在哪？我要干什么？");

    }

    @Test
    public void sendToDirectExchange(){
        User user = new User();
        user.setName("刘会俊");
        user.setAge(24);
        //发送direct消息对象
        rabbitTemplate.convertAndSend("joy.direct.exchange", "queue-3", user);
        user.setName("刘半仙");
        user.setAge(124);
        rabbitTemplate.convertAndSend("joy.direct.exchange", "queue-4", user);
    }

    @Test
    public void sendToTopicExchange(){
        rabbitTemplate.setExchange("joy.topic.exchange");
        User user = new User();
        user.setName("刘二柱");
        user.setAge(18);
        rabbitTemplate.convertAndSend("51.APP.TS",user);
        user.setName("刘一手");
        user.setAge(20);
        rabbitTemplate.convertAndSend("51.WEB.TS",user);
    }

    @Test
    public void sendToNormalExchange(){
        rabbitTemplate.setExchange("joy.normal.direct.exchange");
        User user = new User();
        user.setName("刘三胖");
        user.setAge(18);
        rabbitTemplate.convertAndSend("normal-queue",user);
        user.setName("刘二丫");
        user.setAge(18);
        rabbitTemplate.convertAndSend("normal-queue1",user,message ->
        {
            message.getMessageProperties().setExpiration("10000");
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        user.setName("刘狗剩");
        user.setAge(19);
        rabbitTemplate.convertAndSend("normal-queue1",user);
    }

    @Test
    public void amqpAdminTest(){
        Queue queue = new Queue("admin-test",false,false,false);
        String s = amqpAdmin.declareQueue(queue);
        System.out.println(s);
        Exchange exchange = ExchangeBuilder.directExchange("joy.direct.admin.exchange").durable(false).build();
        amqpAdmin.declareExchange(exchange);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("admin-test").noargs();
        amqpAdmin.declareBinding(binding);
        rabbitTemplate.convertAndSend("joy.direct.admin.exchange","admin-test","hello world");
        amqpAdmin.deleteQueue(queue.getName());
        amqpAdmin.deleteExchange(exchange.getName());
    }
}
