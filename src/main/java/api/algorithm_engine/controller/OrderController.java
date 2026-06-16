package api.algorithm_engine.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import api.algorithm_engine.dto.CreateOrderRequest;
import api.algorithm_engine.model.Order;
import api.algorithm_engine.repository.OrderRepository;
import api.algorithm_engine.service.OrderProducer;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderProducer producer;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateOrderRequest req) {

        Order order = new Order();
        order.setSymbol(req.symbol());
        order.setSide(req.side());
        order.setQuantity(req.quantity());
        order.setCreatedAt(LocalDateTime.now().toString());
        order.setStatus("NEW");

        repository.save(order);
        producer.send(order);
        messagingTemplate.convertAndSend("/topic/order/created", order);

        return ResponseEntity.ok(order.getId());
    }
}