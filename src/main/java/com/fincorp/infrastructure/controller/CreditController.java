package com.fincorp.infrastructure.controller;

import com.fincorp.application.port.in.CreditUseCase;
import com.fincorp.domain.model.Credit;
import com.fincorp.domain.model.CreditHistoryReport;
import com.fincorp.infrastructure.controller.dto.CreateCreditRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de entrada (driving adapter) — capa de infraestructura/web.
 *
 * Endpoints disponibles:
 *   POST   /credits                        → Crear crédito
 *   GET    /credits/all                    → Listar todos
 *   GET    /credits/{creditId}             → Obtener por ID de crédito
 *   GET    /credits/history/{employeeId}   → Historial inteligente por empleado (unifica búsqueda por cédula)
 *   PUT    /credits/{id}                   → Actualizar crédito
 *   DELETE /credits/{id}                   → Eliminar crédito
 *
 * ELIMINADO: GET /credits/by-cedula/{employeeId}
 *   — Reemplazado por GET /credits/history/{employeeId} que cumple función de
 *     búsqueda integral y agrega análisis de historial + credit score.
 */
@RestController
@RequestMapping("/credits")
public class CreditController {

    private final CreditUseCase useCase;

    public CreditController(CreditUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Credit create(@RequestBody CreateCreditRequest request) {
        return useCase.create(
                request.getEmployeeId(),
                request.getEmployeeName(),
                request.getEmail(),
                request.getSalary(),
                request.getAmount(),
                request.getRequestDate(),
                request.getTermMonths()
        );
    }

    @GetMapping("/all")
    public List<Credit> getAll() {
        return useCase.getAll();
    }

    @GetMapping("/{creditId}")
    public Credit getByCreditId(@PathVariable Long creditId) {
        return useCase.getByCreditId(creditId);
    }

    /**
     * Historial de créditos inteligente por empleado.
     * Consolida la funcionalidad del eliminado /by-cedula/{employeeId}
     * agregando resumen estadístico y credit score de dominio.
     */
    @GetMapping("/history/{employeeId}")
    public CreditHistoryReport getHistory(@PathVariable Long employeeId) {
        return useCase.getIntelligentHistory(employeeId);
    }

    @PutMapping("/{id}")
    public Credit update(@PathVariable Long id, @RequestBody CreateCreditRequest request) {
        return useCase.update(
                id,
                request.getEmployeeId(),
                request.getEmployeeName(),
                request.getEmail(),
                request.getSalary(),
                request.getAmount()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        useCase.delete(id);
    }
}
