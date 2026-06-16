package api.algorithm_engine.service.consumers.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.algorithm_engine.config.RabbitConfig;
import api.algorithm_engine.model.Order;
import api.algorithm_engine.service.consumers.DlqService;

@Service
public class EmailConsumer {

    @Autowired
    private DlqService dlqService;

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void consume(Order order) {

        try {
            System.out.println("EMAIL: enviando email para order " + order);

            sendEmail(order);
        } catch (Exception ex) {
            dlqService.save(
                    RabbitConfig.EMAIL_QUEUE,
                    order,
                    ex,
                    order.getId().toString());

            throw new RuntimeException(ex);
        }

    }

    private void sendEmail(Order order) {
        // lógica de email
    }
}