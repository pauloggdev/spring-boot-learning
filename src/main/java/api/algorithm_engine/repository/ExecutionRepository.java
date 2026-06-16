package api.algorithm_engine.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import api.algorithm_engine.model.OrderExecution;

public interface ExecutionRepository extends JpaRepository<OrderExecution, Long> {
    
}
