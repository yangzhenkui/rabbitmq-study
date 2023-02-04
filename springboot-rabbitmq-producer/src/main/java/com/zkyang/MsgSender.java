package com.zkyang;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述： 发送消息
 */
@Component
public class MsgSender {

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    public void send1(){
        String message = "This is message1, routing key is dog.red";
        System.out.println("发送消息" + message);
        this.rabbitmqTemplate.convertAndSend("bootExchange", "dog.red", message);
    }

    public void send2(){
        String message = "This is message2, routing key is dog.black";
        System.out.println("发送消息" + message);
        this.rabbitmqTemplate.convertAndSend("bootExchange", "dog.black", message);
    }
}
