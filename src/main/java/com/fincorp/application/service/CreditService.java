package com.fincorp.application.service;

import com.fincorp.application.port.in.CreditUseCase;
import com.fincorp.application.port.out.CreditRepository;
import com.fincorp.application.port.out.CreditSimulationPort;
import com.fincorp.application.port.out.NotificationPort;
import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.CreditHistoryReport;
import com.fincorp.domain.model.SimulationResult;
import com.fincorp.domain.service.CreditApprovalService;
import com.fincorp.domain.service.CreditHistoryAnalyzer;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de aplicación que implementa CreditUseCase.
 * Orquesta la lógica de dominio sin contener reglas de negocio propias.
 *
 * ELIMINADO: getByEmployeeId — la búsqueda por empleado queda consolidada
 * en getIntelligentHistory, que provee además análisis y credit score.
 */
public class CreditService implements CreditUseCase {

    private final CreditRepository repo;
    private final CreditSimulationPort simulationPort;
    private final NotificationPort notificationPort;
    private final CreditApprovalService approval = new CreditApprovalService();
    private final CreditHistoryAnalyzer historyAnalyzer = new CreditHistoryAnalyzer();

    public CreditService(
            CreditRepository repo,
            CreditSimulationPort simulationPort,
            NotificationPort notificationPort
    ) {
        this.repo = repo;
        this.simulationPort = simulationPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public Credit create(
            Long employeeId,
            String employeeName,
            String email,
            double salary,
            double amount,
            LocalDate requestDate,
            int termMonths
    ) {
        // La evaluación del estado reside en el Dominio (CreditApprovalService)
        String status = approval.evaluate(salary, amount);

        Credit saved = repo.save(new Credit(
                null,
                employeeId,
                employeeName,
                email,
                salary,
                amount,
                status,
                requestDate == null ? LocalDate.now() : requestDate
        ));

        SimulationResult simulation = simulationPort.simulate(salary, amount, termMonths);
        notificationPort.notifySimulation(saved.getEmail(), saved, simulation);

        return saved;
    }

    @Override
    public List<Credit> getAll() {
        return repo.findAll();
    }

    @Override
    public Credit getByCreditId(Long creditId) {
        return repo.findById(creditId);
    }

    /**
     * Retorna el historial inteligente del empleado.
     * Reemplaza la búsqueda plana por cédula, añadiendo análisis de dominio.
     */
    @Override
    public CreditHistoryReport getIntelligentHistory(Long employeeId) {
        List<Credit> previousCredits = repo.findByEmployeeId(employeeId);
        CreditHistoryReport.Summary summary = historyAnalyzer.buildSummary(previousCredits);
        int score = historyAnalyzer.calculateScore(previousCredits, summary);
        String employeeName = previousCredits.isEmpty()
                ? "EMPLEADO-" + employeeId
                : previousCredits.get(0).getEmployeeName();
        return new CreditHistoryReport(employeeName, previousCredits, summary, score);
    }

    @Override
    public Credit update(Long id, Long employeeId, String employeeName, String email, double salary, double amount) {
        String status = approval.evaluate(salary, amount);
        Credit existing = repo.findById(id);
        LocalDate requestDate = existing == null ? LocalDate.now() : existing.getRequestDate();
        return repo.save(new Credit(id, employeeId, employeeName, email, salary, amount, status, requestDate));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
