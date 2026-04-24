import os

from fastapi import FastAPI
from fastapi.responses import HTMLResponse

from app.application.service.simulator_service import SimulatorService
from app.infrastructure.api.schemas import SimulationRequest, SimulationResponse
from app.infrastructure.persistence.sqlite_simulation_repository import SqliteSimulationRepository

app = FastAPI(title="FinCorp - Servicio Simulador de Créditos", version="2.0.0")

database_path = os.getenv("SIMULATOR_DB_PATH", "/data/simulations.db")
repository = SqliteSimulationRepository(database_path=database_path)
service = SimulatorService(repository=repository)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.get("/ui", response_class=HTMLResponse)
def ui() -> str:
    return """
    <!DOCTYPE html>
    <html lang="es">
      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>FinCorp - Simulador de Crédito</title>
        <style>
          *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

          body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f0f4f8;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
          }

          header {
            background: linear-gradient(135deg, #1a237e 0%, #283593 60%, #3949ab 100%);
            color: white;
            padding: 1.5rem 2rem;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.3);
          }

          header h1 {
            font-size: 1.2rem;
            font-weight: 400;
            letter-spacing: 0.03em;
            opacity: 0.9;
            margin-bottom: 0.4rem;
          }

          header h2 {
            font-size: 1.75rem;
            font-weight: 700;
            letter-spacing: 0.01em;
          }

          header .brand {
            font-size: 0.85rem;
            opacity: 0.7;
            margin-top: 0.5rem;
            font-style: italic;
          }

          main {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 2.5rem 1rem;
            gap: 2rem;
            flex-wrap: wrap;
          }

          .card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.1);
            padding: 2rem;
            width: 100%;
            max-width: 460px;
          }

          .card h3 {
            font-size: 1.1rem;
            color: #1a237e;
            margin-bottom: 1.5rem;
            border-bottom: 2px solid #e8eaf6;
            padding-bottom: 0.6rem;
          }

          .field {
            margin-bottom: 1.25rem;
          }

          label {
            display: block;
            font-size: 0.85rem;
            font-weight: 600;
            color: #37474f;
            margin-bottom: 0.35rem;
          }

          input[type="number"] {
            width: 100%;
            padding: 0.6rem 0.9rem;
            border: 1.5px solid #cfd8dc;
            border-radius: 8px;
            font-size: 1rem;
            color: #263238;
            transition: border-color 0.2s;
            background: #fafafa;
          }

          input[type="number"]:focus {
            outline: none;
            border-color: #3949ab;
            background: white;
          }

          button[type="submit"] {
            width: 100%;
            padding: 0.85rem;
            background: linear-gradient(135deg, #1a237e, #3949ab);
            color: white;
            font-size: 1rem;
            font-weight: 600;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            letter-spacing: 0.03em;
            transition: opacity 0.2s, transform 0.1s;
            margin-top: 0.5rem;
          }

          button[type="submit"]:hover { opacity: 0.9; }
          button[type="submit"]:active { transform: scale(0.98); }

          #result-card {
            display: none;
          }

          .result-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.6rem 0;
            border-bottom: 1px solid #eceff1;
            font-size: 0.95rem;
          }

          .result-row:last-child { border-bottom: none; }

          .result-row .label { color: #546e7a; }
          .result-row .value { font-weight: 600; color: #263238; }

          .status-badge {
            display: inline-block;
            padding: 0.3rem 1rem;
            border-radius: 20px;
            font-weight: 700;
            font-size: 1rem;
            letter-spacing: 0.05em;
          }

          .status-aceptado {
            background: #e8f5e9;
            color: #2e7d32;
            border: 1.5px solid #a5d6a7;
          }

          .status-rechazado {
            background: #ffebee;
            color: #c62828;
            border: 1.5px solid #ef9a9a;
          }

          .spinner {
            display: none;
            text-align: center;
            padding: 1rem;
            color: #5c6bc0;
            font-size: 0.9rem;
          }

          footer {
            text-align: center;
            padding: 1rem;
            font-size: 0.78rem;
            color: #90a4ae;
          }
        </style>
      </head>
      <body>
        <header>
          <h1>Simula tu crédito y piensa dos veces antes de tomar la decisión de ser parte de nuestra compañía FinCorp</h1>
          <h2>🏦 Simulador de Crédito</h2>
          <p class="brand">FinCorp · Arquitectura Hexagonal</p>
        </header>

        <main>
          <div class="card">
            <h3>📋 Datos de la Simulación</h3>
            <div class="field">
              <label for="salary">Salario Mensual (COP $)</label>
              <input type="number" id="salary" value="5000000" min="1" required />
            </div>
            <div class="field">
              <label for="requested_amount">Monto Solicitado (COP $)</label>
              <input type="number" id="requested_amount" value="1200000" min="1" required />
            </div>
            <div class="field">
              <label for="term_months">Plazo (meses, máx. 84)</label>
              <input type="number" id="term_months" value="24" min="1" max="84" required />
            </div>
            <button type="submit" onclick="simular()">Calcular Simulación</button>
            <div class="spinner" id="spinner">⏳ Calculando...</div>
          </div>

          <div class="card" id="result-card">
            <h3>📊 Resultado de la Simulación</h3>

            <div class="result-row">
              <span class="label">Estado del Crédito</span>
              <span class="value"><span class="status-badge" id="status-badge">—</span></span>
            </div>
            <div class="result-row">
              <span class="label">Monto Máximo Permitido</span>
              <span class="value" id="res-max-amount">—</span>
            </div>
            <div class="result-row">
              <span class="label">Monto Solicitado</span>
              <span class="value" id="res-requested">—</span>
            </div>
            <div class="result-row">
              <span class="label">Plazo</span>
              <span class="value" id="res-term">—</span>
            </div>
            <div class="result-row">
              <span class="label">Tasa de Interés Mensual</span>
              <span class="value" id="res-rate">—</span>
            </div>
            <div class="result-row">
              <span class="label">Cuota Mensual Estimada</span>
              <span class="value" id="res-installment">—</span>
            </div>
            <div class="result-row">
              <span class="label">Total de Intereses</span>
              <span class="value" id="res-total-interest">—</span>
            </div>
            <div class="result-row" style="background:#f3f4ff; border-radius:8px; padding: 0.7rem 0.5rem; margin-top:0.5rem;">
              <span class="label" style="font-weight:700; color:#1a237e;">💰 Total a Pagar (Capital + Intereses)</span>
              <span class="value" style="font-size:1.1rem; color:#1a237e;" id="res-total-pay">—</span>
            </div>
          </div>
        </main>

        <footer>FinCorp &copy; 2026 · Microservicio Simulador de Créditos v2.0</footer>

        <script>
          const fmt = (n) => new Intl.NumberFormat('es-CO', {style:'currency', currency:'COP', maximumFractionDigits:0}).format(n);
          const pct = (n) => (n * 100).toFixed(2) + '%';

          async function simular() {
            const salary = Number(document.getElementById('salary').value);
            const requested_amount = Number(document.getElementById('requested_amount').value);
            const term_months = Number(document.getElementById('term_months').value);

            if (!salary || !requested_amount || !term_months) {
              alert('Por favor completa todos los campos.');
              return;
            }

            document.getElementById('spinner').style.display = 'block';
            document.getElementById('result-card').style.display = 'none';

            try {
              const response = await fetch('/simulations', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ salary, requested_amount, term_months })
              });

              if (!response.ok) throw new Error('Error en la simulación: ' + response.status);

              const data = await response.json();

              // Estado con color desde dominio
              const badge = document.getElementById('status-badge');
              badge.textContent = data.status;
              badge.className = 'status-badge ' + (data.status === 'ACEPTADO' ? 'status-aceptado' : 'status-rechazado');

              document.getElementById('res-max-amount').textContent = fmt(data.max_amount);
              document.getElementById('res-requested').textContent = fmt(requested_amount);
              document.getElementById('res-term').textContent = data.term_months + ' meses';
              document.getElementById('res-rate').textContent = pct(data.applied_interest_rate) + ' mensual';
              document.getElementById('res-installment').textContent = fmt(data.estimated_installment);
              document.getElementById('res-total-interest').textContent = fmt(data.total_interest);
              document.getElementById('res-total-pay').textContent = fmt(data.total_to_pay);

              document.getElementById('result-card').style.display = 'block';
            } catch (err) {
              alert('Error al simular: ' + err.message);
            } finally {
              document.getElementById('spinner').style.display = 'none';
            }
          }
        </script>
      </body>
    </html>
    """


@app.post("/simulations", response_model=SimulationResponse)
def simulate(request: SimulationRequest) -> SimulationResponse:
    simulation = service.simulate(
        salary=request.salary,
        requested_amount=request.requested_amount,
        term_months=request.term_months,
    )
    return SimulationResponse(
        max_amount=simulation.max_amount,
        estimated_installment=simulation.estimated_installment,
        applied_interest_rate=simulation.applied_interest_rate,
        term_months=simulation.term_months,
        total_interest=simulation.total_interest,
        total_to_pay=simulation.total_to_pay,
        status=simulation.status,
    )
