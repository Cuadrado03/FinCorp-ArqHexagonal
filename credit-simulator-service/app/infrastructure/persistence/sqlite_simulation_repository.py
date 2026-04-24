import sqlite3

from app.application.port.out.simulation_repository import SimulationRepository
from app.domain.model.simulation import Simulation


class SqliteSimulationRepository(SimulationRepository):
    """
    Adaptador de salida (driven adapter) — persistencia SQLite para simulaciones.
    Almacena los campos calculados en el dominio: status, total_interest, total_to_pay.
    """

    def __init__(self, database_path: str) -> None:
        self._database_path = database_path
        self._initialize()

    def _initialize(self) -> None:
        with sqlite3.connect(self._database_path) as conn:
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS simulations (
                    id                   INTEGER PRIMARY KEY AUTOINCREMENT,
                    salary               REAL    NOT NULL,
                    requested_amount     REAL    NOT NULL,
                    max_amount           REAL    NOT NULL,
                    estimated_installment REAL   NOT NULL,
                    applied_interest_rate REAL   NOT NULL,
                    term_months          INTEGER NOT NULL,
                    total_interest       REAL    NOT NULL DEFAULT 0,
                    total_to_pay         REAL    NOT NULL DEFAULT 0,
                    status               TEXT    NOT NULL DEFAULT 'RECHAZADO'
                )
                """
            )
            conn.commit()

    def save(self, simulation: Simulation) -> Simulation:
        with sqlite3.connect(self._database_path) as conn:
            cursor = conn.execute(
                """
                INSERT INTO simulations (
                    salary, requested_amount, max_amount,
                    estimated_installment, applied_interest_rate, term_months,
                    total_interest, total_to_pay, status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                (
                    simulation.salary,
                    simulation.requested_amount,
                    simulation.max_amount,
                    simulation.estimated_installment,
                    simulation.applied_interest_rate,
                    simulation.term_months,
                    simulation.total_interest,
                    simulation.total_to_pay,
                    simulation.status,
                ),
            )
            conn.commit()
            generated_id = cursor.lastrowid

        return Simulation(
            id=generated_id,
            salary=simulation.salary,
            requested_amount=simulation.requested_amount,
            max_amount=simulation.max_amount,
            estimated_installment=simulation.estimated_installment,
            applied_interest_rate=simulation.applied_interest_rate,
            term_months=simulation.term_months,
        )
