package api.algorithm_engine.service.consumers;

import org.springframework.stereotype.Service;

import api.algorithm_engine.model.DlqErrors;
import api.algorithm_engine.repository.DlqErrorsRepository;

@Service
public class DlqService {

    private final DlqErrorsRepository repository;

    public DlqService(DlqErrorsRepository repository) {
        this.repository = repository;
    }

    public void save(String queueName, Object payload, Exception e, String orderId) {

        DlqErrors dlq = new DlqErrors();
        dlq.setOrderId(orderId);
        dlq.setQueueName(queueName);
        dlq.setPayload(payload != null ? payload.toString() : null);
        dlq.setErrorReason(e.getMessage());
        dlq.setStackTrace(getStackTrace(e));

        dlq.setRetryCount(1); // primeiro erro

        repository.save(dlq);
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement el : e.getStackTrace()) {
            sb.append(el.toString()).append("\n");
        }
        return sb.toString();
    }
}