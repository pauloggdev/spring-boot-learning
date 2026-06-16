package api.algorithm_engine.service.consumers.order;

import java.time.LocalDateTime;

import org.aspectj.bridge.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.algorithm_engine.config.RabbitConfig;
import api.algorithm_engine.model.DlqErrors;
import api.algorithm_engine.model.Order;
import api.algorithm_engine.model.OrderExecution;
import api.algorithm_engine.repository.DlqErrorsRepository;
import api.algorithm_engine.repository.ExecutionRepository;
import api.algorithm_engine.repository.OrderRepository;
import api.algorithm_engine.service.consumers.DlqService;

@Service
public class OrderConsumer {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private ExecutionRepository executionRepository;
    @Autowired
    private DlqService dlqService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void consume(Order order) {

        try {
            System.out.println("Processando ordem: " + order.getId());
            order.setStatus("DONE");
            repository.save(order);

            OrderExecution exec = new OrderExecution();
            exec.setOrderId(order.getId());
            exec.setResult("EXECUTED");
            exec.setExecutedAt(LocalDateTime.now());
            executionRepository.save(exec);
            System.out.println("Ordem executada: " + order.getId());

        } catch (Exception ex) {
            dlqService.save(
                    RabbitConfig.ORDER_QUEUE,
                    order,
                    ex,
                    order.getId().toString());

            throw new RuntimeException(ex);

        }

    }

}
