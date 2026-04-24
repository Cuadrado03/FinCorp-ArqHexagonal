from pydantic import BaseModel, Field


class SimulationRequest(BaseModel):
    salary: float = Field(gt=0, description="Salario mensual del empleado")
    requested_amount: float = Field(gt=0, description="Monto solicitado del crédito")
    term_months: int = Field(gt=0, le=84, description="Plazo en meses (máximo 84)")


class SimulationResponse(BaseModel):
    max_amount: float
    estimated_installment: float
    applied_interest_rate: float
    term_months: int
    total_interest: float
    total_to_pay: float
    status: str  # "ACEPTADO" o "RECHAZADO" — calculado en el Dominio
