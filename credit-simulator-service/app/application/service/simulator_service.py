from app.application.port.out.simulation_repository import SimulationRepository
from app.domain.model.simulation import Simulation


class SimulatorService:
    """
    Servicio de aplicación del simulador.
    Delega el cálculo del estado (ACEPTADO/RECHAZADO) al modelo de Dominio (Simulation).
    """

    def __init__(self, repository: SimulationRepository) -> None:
        self._repository = repository

    def simulate(self, salary: float, requested_amount: float, term_months: int) -> Simulation:
        months = max(1, int(term_months))
        max_amount = salary * 0.3

        # Tasa diferencial según monto solicitado vs máximo permitido
        base_interest = 0.015 if requested_amount <= max_amount else 0.025

        total_factor = 1 + (base_interest * months)
        estimated_installment = round((requested_amount * total_factor) / months, 2)

        # El Dominio calcula: status, total_interest, total_to_pay
        simulation = Simulation(
            id=None,
            salary=salary,
            requested_amount=requested_amount,
            max_amount=max_amount,
            estimated_installment=estimated_installment,
            applied_interest_rate=base_interest,
            term_months=months,
        )

        return self._repository.save(simulation)
