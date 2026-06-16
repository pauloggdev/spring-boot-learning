package api.algorithm_engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.algorithm_engine.model.AuditLog;
import api.algorithm_engine.repository.AuditLogRepository;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository repository;

    public void save(
            Long orderId,
            String queueName,
            String action,
            String status,
            String message) {

        AuditLog log = new AuditLog();

        log.setOrderId(orderId);
        log.setQueueName(queueName);
        log.setAction(action);
        log.setStatus(status);
        log.setMessage(message);
        repository.save(log);
    }
}
