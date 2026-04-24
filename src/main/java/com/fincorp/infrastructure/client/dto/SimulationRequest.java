package com.fincorp.infrastructure.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SimulationRequest {
    private final double salary;
    private final double requestedAmount;
    private final int termMonths;

    public SimulationRequest(double salary, double requestedAmount, int termMonths) {
        this.salary = salary;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
    }

    public double getSalary() {
        return salary;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public int getTermMonths() {
        return termMonths;
    }
}
