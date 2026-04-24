package com.fincorp.infrastructure.persistence.repository;
import com.fincorp.infrastructure.persistence.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataCreditRepository extends JpaRepository<CreditEntity, Long> {
    List<CreditEntity> findByEmployeeId(Long employeeId);
}