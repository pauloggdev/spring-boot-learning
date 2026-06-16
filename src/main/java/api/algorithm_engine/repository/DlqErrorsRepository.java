package api.algorithm_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.algorithm_engine.model.DlqErrors;

public interface DlqErrorsRepository extends JpaRepository<DlqErrors, Long> {
    
}
