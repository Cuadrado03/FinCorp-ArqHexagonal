from abc import ABC, abstractmethod

from app.domain.model.simulation import Simulation


class SimulationRepository(ABC):
    @abstractmethod
    def save(self, simulation: Simulation) -> Simulation:
        raise NotImplementedError
