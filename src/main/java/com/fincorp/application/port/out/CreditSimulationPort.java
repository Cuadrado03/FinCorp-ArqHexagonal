package com.fincorp.application.port.out;

import com.fincorp.domain.model.SimulationResult;

public interface CreditSimulationPort {
    SimulationResult simulate(double salary, double amount, int termMonths);
}
