package com.moss.server.sender;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Sender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String routtingKey,String msg){
		rabbitTemplate.convertAndSend("topicExchange", routtingKey, msg.getBytes() );
	}
}