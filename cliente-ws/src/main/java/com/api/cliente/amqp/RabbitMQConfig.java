package com.api.cliente.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Bean
    @Qualifier("CLIENTE")
    public Queue clientQueue() { return new Queue("CLIENTE"); }

    @Bean
    public ClienteProducer clienteProducer() { return new ClienteProducer(); }

    @Bean
    public ClienteReceiver clienteReceiver() { return new ClienteReceiver(); }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap();
        classMapper.setTrustedPackages("*");

        idClassMapping.put("com.api.saga.amqp.ClienteTransfer", ClienteTransfer.class);

        classMapper.setIdClassMapping(idClassMapping);

        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());

        return converter;
    }
}
