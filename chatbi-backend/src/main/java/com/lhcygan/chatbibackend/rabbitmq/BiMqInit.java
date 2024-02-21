package com.lhcygan.chatbibackend.rabbitmq;

import com.lhcygan.chatbibackend.constant.BiMqConstant;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建队列程序的交换机和队列
 */
@Configuration
public class BiMqInit {

//    public static void main(String[] args) {
//        try {
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost("172.19.222.28");
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//
//            String biExchange = BiMqConstant.BI_EXCHANGE_NAME;
//            channel.exchangeDeclare(biExchange, BiMqConstant.BI_DIRECT_EXCHANGE);
//
//            // 创建队列，分配一个队列名称：demo_queue
//            String queueName = BiMqConstant.BI_QUEUE_NAME;
//            channel.queueDeclare(queueName, true, false, false, null);
//            channel.queueBind(queueName, biExchange, BiMqConstant.BI_ROUTING_KEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 图表分析
     */
    /**
     * 将死信队列和交换机声明
     */
    @Bean
    Queue biDeadQueue() {
        return QueueBuilder.durable(BiMqConstant.BI_DEAD_QUEUE_NAME).build();
    }

    @Bean
    DirectExchange biDeadExchange() {
        return new DirectExchange(BiMqConstant.BI_DEAD_EXCHANGE_NAME);
    }

    /**
     * 将队列和交换机声明
     */
    @Bean
    Queue biQueue() {
        Map<String, Object> arg = new HashMap<>();
        arg.put("x-message-ttl", 60000);
        // 绑定死信交换机
        arg.put("x-dead-letter-exchange", BiMqConstant.BI_DEAD_EXCHANGE_NAME);
        arg.put("x-dead-letter-routing-key", BiMqConstant.BI_DEAD_ROUTING_KEY);
        return QueueBuilder.durable(BiMqConstant.BI_QUEUE_NAME).withArguments(arg).build();
    }
    
    @Bean
    DirectExchange biExchange() {
        return new DirectExchange(BiMqConstant.BI_EXCHANGE_NAME);
    }
    
    @Bean
    Binding BiBinding(Queue biQueue, DirectExchange biExchange) {
        return BindingBuilder.bind(biQueue).to(biExchange).with(BiMqConstant.BI_ROUTING_KEY);
    }

}
