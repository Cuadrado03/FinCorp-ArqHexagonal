package com.fincorp.infrastructure.controller.dto;

import java.time.LocalDate;

public class CreateCreditRequest {
    private Long employeeId;
    private String employeeName;
    public Long getEmployeeId() {
        return employeeId;
    }

    private String email;
    private double salary;
    private double amount;
    private LocalDate requestDate;
    private int termMonths = 12;

    public String getEmployeeName() {
        return employeeName;
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

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public int getTermMonths() {
        return termMonths;
    }
}
