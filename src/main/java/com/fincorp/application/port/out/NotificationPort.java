package com.fincorp.application.port.out;

import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.SimulationResult;

public interface NotificationPort {
    void notifySimulation(String targetEmail, Credit credit, SimulationResult simulationResult);
}
