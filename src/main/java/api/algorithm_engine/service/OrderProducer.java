package api.algorithm_engine.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.algorithm_engine.config.RabbitConfig;
import api.algorithm_engine.model.Order;

@Service
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.ORDER_CREATED,
                order);

        // rabbitTemplate.convertAndSend(
        // RabbitConfig.ORDER_QUEUE,
        // order
        // );
    }
}
