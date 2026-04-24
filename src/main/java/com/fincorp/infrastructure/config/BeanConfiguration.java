package com.fincorp.infrastructure.config;

import com.fincorp.application.port.in.CreditUseCase;
import com.fincorp.application.port.out.CreditRepository;
import com.fincorp.application.port.out.CreditSimulationPort;
import com.fincorp.application.port.out.NotificationPort;
import com.fincorp.application.service.CreditService;
import com.fincorp.infrastructure.persistence.adapter.CreditPersistenceAdapter;
import com.fincorp.infrastructure.persistence.repository.SpringDataCreditRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreditRepository creditRepository(SpringDataCreditRepository r) {
        return new CreditPersistenceAdapter(r);
    }

    @Bean
    public CreditUseCase creditUseCase(
            CreditRepository creditRepository,
            CreditSimulationPort simulationPort,
            NotificationPort notificationPort) {
        return new CreditService(creditRepository, simulationPort, notificationPort);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
