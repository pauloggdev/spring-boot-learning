package api.algorithm_engine.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";

    // Queues
    public static final String ORDER_QUEUE = "order.queue";
    public static final String AUDIT_QUEUE = "audit.queue";
    public static final String EMAIL_QUEUE = "email.queue";

    // Routing keys (EVENTOS)
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_AUDITED = "order.audited";

    // DLQ
    public static final String ORDER_DLX = "order.dlx";
    public static final String ORDER_DLQ = "order.dlq";
    public static final String AUDIT_DLQ = "audit.dlq";
    public static final String EMAIL_DLQ = "email.dlq";

    // EXCHANGE
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // ===================== QUEUES =====================

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .deadLetterExchange(ORDER_DLX)
                .deadLetterRoutingKey(ORDER_DLQ)
                .build();
    }

    @Bean
    public Queue auditQueue() {
        return QueueBuilder.durable(AUDIT_QUEUE)
                .deadLetterExchange(ORDER_DLX)
                .deadLetterRoutingKey(AUDIT_DLQ)
                .build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .deadLetterExchange(ORDER_DLX)
                .deadLetterRoutingKey(EMAIL_DLQ)
                .build();
    }

    // ===================== BINDINGS =====================

    // Producer → Audit (primeiro passo)
    @Bean
    public Binding auditBinding() {
        return BindingBuilder
                .bind(auditQueue())
                .to(orderExchange())
                .with(ORDER_CREATED);
    }

    // Audit → Email (segundo passo)
    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(orderExchange())
                .with(ORDER_AUDITED);
    }

    // (opcional) manter order queue
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_CREATED);
    }

    // ===================== DLQ =====================

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(ORDER_DLX);
    }

    @Bean
    public Queue orderDLQ() {
        return new Queue(ORDER_DLQ);
    }

    @Bean
    public Queue auditDLQ() {
        return new Queue(AUDIT_DLQ);
    }

    @Bean
    public Queue emailDLQ() {
        return new Queue(EMAIL_DLQ);
    }

    @Bean
    public Binding orderDLQBinding() {
        return BindingBuilder
                .bind(orderDLQ())
                .to(deadLetterExchange())
                .with(ORDER_DLQ);
    }

    @Bean
    public Binding auditDLQBinding() {
        return BindingBuilder
                .bind(auditDLQ())
                .to(deadLetterExchange())
                .with(AUDIT_DLQ);
    }

    @Bean
    public Binding emailDLQBinding() {
        return BindingBuilder
                .bind(emailDLQ())
                .to(deadLetterExchange())
                .with(EMAIL_DLQ);
    }

    // ===================== SERIALIZATION =====================

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}