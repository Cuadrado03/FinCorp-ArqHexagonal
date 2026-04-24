package com.fincorp.infrastructure.persistence.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long employeeId;
    public String employeeName;
    public String email;
    public double salary;
    public double amount;
    public String status;
    public LocalDate requestDate;
}