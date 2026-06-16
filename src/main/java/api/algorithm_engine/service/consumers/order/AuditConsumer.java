package api.algorithm_engine.service.consumers.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.algorithm_engine.config.RabbitConfig;
import api.algorithm_engine.model.Order;
import api.algorithm_engine.service.AuditService;
import api.algorithm_engine.service.consumers.DlqService;

@Service
public class AuditConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DlqService dlqService;
    @Autowired
    private AuditService auditService;

    @RabbitListener(queues = RabbitConfig.AUDIT_QUEUE)
    public void consume(Order order) {

        try {
            System.out.println("AUDIT: feito auditoria " + order);

            auditService.save(
                    order.getId(),
                    RabbitConfig.AUDIT_QUEUE,
                    "AUDIT_STARTED",
                    "SUCCESS",
                    "Processamento iniciado");

            auditService.save(
                    order.getId(),
                    RabbitConfig.AUDIT_QUEUE,
                    "AUDIT_FINISHED",
                    "SUCCESS",
                    "Auditoria concluída");

            rabbitTemplate.convertAndSend(
                    RabbitConfig.ORDER_EXCHANGE,
                    RabbitConfig.ORDER_AUDITED,
                    order);

        } catch (Exception ex) {
            dlqService.save(
                    RabbitConfig.AUDIT_QUEUE,
                    order,
                    ex,
                    order.getId().toString());
            auditService.save(
                    order.getId(),
                    RabbitConfig.AUDIT_QUEUE,
                    "AUDIT_FAILED",
                    "ERROR",
                    ex.getMessage());

        }
    }

}