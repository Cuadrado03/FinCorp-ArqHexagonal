package com.fincorp.domain.model;

import java.time.LocalDate;

public class Credit {

    private final Long id;
    private final Long employeeId;
    private final String employeeName;
    private final String email;
    private final double salary;
    private final double amount;
    private final String status;
    private final LocalDate requestDate;

    public Credit(
            Long id,
            Long employeeId,
            String employeeName,
            String email,
            double salary,
            double amount,
            String status,
            LocalDate requestDate
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.email = email;
        this.salary = salary;
        this.amount = amount;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }
}