package api.algorithm_engine.model;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "dlq_errors")
public class DlqErrors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(length = 1000, nullable = false)
    private String errorReason;

    @Column(nullable = false)
    private String queueName;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private Integer retryCount = 0;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String stackTrace;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;

}