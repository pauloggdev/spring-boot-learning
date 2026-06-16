package api.algorithm_engine.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import api.algorithm_engine.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
