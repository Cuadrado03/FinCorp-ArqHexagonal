package com.fincorp.application.port.in;

import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.CreditHistoryReport;

import java.time.LocalDate;
import java.util.List;

/**
 * Puerto de entrada (driving port) para los casos de uso de crédito.
 * Se elimina getByEmployeeId para consolidar la búsqueda por empleado
 * en el endpoint de historial inteligente (getIntelligentHistory).
 */
public interface CreditUseCase {

    Credit create(
            Long employeeId,
            String employeeName,
            String email,
            double salary,
            double amount,
            LocalDate requestDate,
            int termMonths
    );

    List<Credit> getAll();

    Credit getByCreditId(Long creditId);

    /**
     * Historial de créditos por empleado con análisis y score.
     * Reemplaza y consolida el anterior endpoint /by-cedula/{employeeId}.
     */
    CreditHistoryReport getIntelligentHistory(Long employeeId);

    Credit update(Long id, Long employeeId, String employeeName, String email, double salary, double amount);

    void delete(Long id);
}
