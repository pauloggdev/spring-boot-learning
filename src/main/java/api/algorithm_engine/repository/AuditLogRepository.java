package api.algorithm_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api.algorithm_engine.model.AuditLog;

@Repository
public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
}
