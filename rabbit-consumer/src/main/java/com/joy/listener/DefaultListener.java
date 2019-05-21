package com.joy.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-20 14:02
 */
@Component
public class DefaultListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}

