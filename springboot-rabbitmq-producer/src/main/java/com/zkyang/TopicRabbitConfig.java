package com.zkyang;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {

    @Bean
    public Queue queue1(){
        return new Queue("queue1");
    }


    @Bean
    public Queue queue2(){
        return new Queue("queue2");
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange("bootExchange");
    }


    // 将队列绑定到交换机上
    @Bean
    Binding bindingExchangeMessage1(Queue queue1, TopicExchange exchange){
        return BindingBuilder.bind(queue1).to(exchange).with("dog.red");
    }

    @Bean
    Binding bindingExchangeMessage2(Queue queue2, TopicExchange exchange){
        return BindingBuilder.bind(queue2).to(exchange).with("dog.#");
    }
}
