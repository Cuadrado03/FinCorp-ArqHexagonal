package com.fincorp.infrastructure.notification;

import com.fincorp.application.port.out.NotificationPort;
import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.SimulationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationAdapter implements NotificationPort {

    private final JavaMailSender mailSender;
    private final String sender;

    public EmailNotificationAdapter(
            JavaMailSender mailSender,
            @Value("${notifications.email.sender}") String sender
    ) {
        this.mailSender = mailSender;
        this.sender = sender;
    }

    @Override
    @Async
    public void notifySimulation(String targetEmail, Credit credit, SimulationResult simulationResult) {
        if (targetEmail == null || targetEmail.isBlank()) {
            return;
        }

        double maxDomainAmount = Math.round(credit.getSalary() * 0.30 * 100.0) / 100.0;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(targetEmail);
        message.setSubject("Resultado de simulacion de credito - FinCorp");
        message.setText(
                "Hola " + credit.getEmployeeName() + ",\n\n" +
                        "Tu simulacion fue procesada.\n" +
                        "Monto solicitado: " + credit.getAmount() + "\n" +
                        "Monto maximo permitido (regla dominio 30%): " + maxDomainAmount + "\n" +
                        "Capacidad de endeudamiento estimada (simulador): " + simulationResult.getMaxAmount() + "\n" +
                        "Cuota estimada: " + simulationResult.getEstimatedInstallment() + "\n" +
                        "Interes aplicado: " + simulationResult.getAppliedInterestRate() + "\n" +
                        "Plazo (meses): " + simulationResult.getTermMonths() + "\n" +
                        "Estado de evaluacion: " + credit.getStatus()
        );
        mailSender.send(message);
    }
}
