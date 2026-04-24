package com.fincorp.infrastructure.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SimulationResponse {
    private double maxAmount;
    private double estimatedInstallment;
    private double appliedInterestRate;
    private int termMonths;

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getEstimatedInstallment() {
        return estimatedInstallment;
    }

    public void setEstimatedInstallment(double estimatedInstallment) {
        this.estimatedInstallment = estimatedInstallment;
    }

    public double getAppliedInterestRate() {
        return appliedInterestRate;
    }

    public void setAppliedInterestRate(double appliedInterestRate) {
        this.appliedInterestRate = appliedInterestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }
}
