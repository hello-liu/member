package com.moss.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gaomin on 2017/12/1.
 */
@Configuration
public class RabbitmqConfig {

   //声明队列
    @Bean
    public Queue queueLog() {
       return new Queue("moss.queue.log", true); // true表示持久化该队列
   }

    //声明交互器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    //绑定
    @Bean
    public Binding bindingLog() {
        return BindingBuilder.bind(queueLog()).to(topicExchange()).with("log.#");
    }
}