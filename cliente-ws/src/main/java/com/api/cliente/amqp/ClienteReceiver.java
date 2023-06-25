package com.api.cliente.amqp;

import com.api.cliente.dtos.ClienteDto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Objects;
import java.util.UUID;

@RabbitListener(queues = "CLIENTE")
public class ClienteReceiver {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ClienteProducer clienteProducer;
    @Autowired
    private ClienteHelper clienteHelper;
    @RabbitListener(queues = "CLIENTE")
    public void print(String msg) {
        System.out.println(msg);
    }
}
