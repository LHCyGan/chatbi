package com.lhcygan.chatbibackend.rabbitmq;

import com.lhcygan.chatbibackend.constant.BiMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BiMqInit {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("172.19.222.28");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String biExchange = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(biExchange, BiMqConstant.BI_DIRECT_EXCHANGE);

            // 创建队列，分配一个队列名称：demo_queue
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, biExchange, BiMqConstant.BI_ROUTING_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
