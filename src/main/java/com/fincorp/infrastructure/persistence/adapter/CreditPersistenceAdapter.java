package com.fincorp.infrastructure.persistence.adapter;

import com.fincorp.application.port.out.CreditRepository;
import com.fincorp.domain.model.Credit;
import com.fincorp.infrastructure.persistence.entity.CreditEntity;
import com.fincorp.infrastructure.persistence.repository.SpringDataCreditRepository;
import java.util.List;
import java.util.stream.Collectors;

public class CreditPersistenceAdapter implements CreditRepository {

    private final SpringDataCreditRepository repo;

    public CreditPersistenceAdapter(SpringDataCreditRepository repo) {
        this.repo = repo;
    }

    public Credit save(Credit credit) {
        CreditEntity entity = new CreditEntity();
        entity.id = credit.getId();
        entity.employeeId = credit.getEmployeeId();
        entity.employeeName = credit.getEmployeeName();
        entity.email = credit.getEmail();
        entity.salary = credit.getSalary();
        entity.amount = credit.getAmount();
        entity.status = credit.getStatus();
        entity.requestDate = credit.getRequestDate();
        CreditEntity saved = repo.save(entity);
        return mapToDomain(saved);
    }

    public List<Credit> findAll() {
        return repo.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    public Credit findById(Long id) {
        return repo.findById(id).map(this::mapToDomain).orElse(null);
    }

    public List<Credit> findByEmployeeId(Long employeeId) {
        return repo.findByEmployeeId(employeeId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    private Credit mapToDomain(CreditEntity e) {
        return new Credit(
                e.id,
                e.employeeId,
                e.employeeName,
                e.email,
                e.salary,
                e.amount,
                e.status,
                e.requestDate
        );
    }
}
