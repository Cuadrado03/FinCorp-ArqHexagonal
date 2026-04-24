from dataclasses import dataclass, field


@dataclass(frozen=True)
class Simulation:
    """
    Modelo de dominio para el resultado de una simulación de crédito.
    El estado (ACEPTADO/RECHAZADO) se calcula aquí, en el Dominio,
    y no en el controlador ni en el servicio de aplicación.
    """
    id: int | None
    salary: float
    requested_amount: float
    max_amount: float
    estimated_installment: float
    applied_interest_rate: float
    term_months: int

    # Calculados en el dominio
    total_interest: float = field(default=0.0)
    total_to_pay: float = field(default=0.0)
    status: str = field(default="RECHAZADO")

    def __post_init__(self) -> None:
        # Python frozen dataclass: usamos object.__setattr__ para calcular derivados
        total_interest = round(
            self.requested_amount * self.applied_interest_rate * self.term_months, 2
        )
        total_to_pay = round(self.requested_amount + total_interest, 2)
        status = "ACEPTADO" if self.requested_amount <= self.max_amount else "RECHAZADO"

        object.__setattr__(self, "total_interest", total_interest)
        object.__setattr__(self, "total_to_pay", total_to_pay)
        object.__setattr__(self, "status", status)
