package com.fincorp.domain.model;

public class SimulationResult {
    private final double maxAmount;
    private final double estimatedInstallment;
    private final double appliedInterestRate;
    private final int termMonths;

    public SimulationResult(double maxAmount, double estimatedInstallment, double appliedInterestRate, int termMonths) {
        this.maxAmount = maxAmount;
        this.estimatedInstallment = estimatedInstallment;
        this.appliedInterestRate = appliedInterestRate;
        this.termMonths = termMonths;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public double getEstimatedInstallment() {
        return estimatedInstallment;
    }

    public double getAppliedInterestRate() {
        return appliedInterestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }
}
