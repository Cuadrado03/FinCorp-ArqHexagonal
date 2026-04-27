package com.fincorp.infrastructure.client;

import com.fincorp.application.port.out.CreditSimulationPort;
import com.fincorp.domain.model.SimulationResult;
import com.fincorp.infrastructure.client.dto.SimulationRequest;
import com.fincorp.infrastructure.client.dto.SimulationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CreditSimulationHttpAdapter implements CreditSimulationPort {

    private final RestTemplate restTemplate;
    private final String simulatorBaseUrl;

    public CreditSimulationHttpAdapter(
            RestTemplate restTemplate,
            @Value("${services.simulator.base-url}") String simulatorBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.simulatorBaseUrl = simulatorBaseUrl;
    }

    @Override
    public SimulationResult simulate(double salary, double amount, int termMonths) {
        SimulationResponse response = restTemplate.postForObject(
                simulatorBaseUrl + "/simulations",
                new SimulationRequest(salary, amount, termMonths),
                SimulationResponse.class
        );

        if (response == null) {
            return new SimulationResult(0, 0, 0, termMonths);
        }

        return new SimulationResult(
                response.getMaxAmount(),
                response.getEstimatedInstallment(),
                response.getAppliedInterestRate(),
                response.getTermMonths()
        );
    }
}
