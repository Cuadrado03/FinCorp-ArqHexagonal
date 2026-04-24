package com.fincorp.application.port.out;
import com.fincorp.domain.model.Credit;
import java.util.List;

public interface CreditRepository {
    Credit save(Credit credit);
    List<Credit> findAll();
    Credit findById(Long id);
    List<Credit> findByEmployeeId(Long employeeId);
    void deleteById(Long id);
}
