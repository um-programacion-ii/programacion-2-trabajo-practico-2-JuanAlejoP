package com.JuanAlejoP.biblioteca.alerts;

import java.util.concurrent.*;

/**
 * Scheduler de alertas que programa la ejecución periódica de las alertas de vencimiento
 * y disponibilidad de recursos.
 */
public class AlertScheduler {
    /**
     * Servicio programador que ejecuta tareas de forma periódica.
     */
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Alerta que revisa préstamos próximos a vencer.
     */
    private AlertaVencimiento alertaVencimiento;

    /**
     * Alerta que revisa recursos disponibles.
     */
    private AlertaDisponibilidad alertaDisponibilidad;

    /**
     * Construye un programador de alertas con las instancias de alerta de vencimiento
     * y disponibilidad.
     *
     * @param av instancia de {@link AlertaVencimiento} para manejar alertas de vencimiento
     * @param ad instancia de {@link AlertaDisponibilidad} para manejar alertas de disponibilidad
     */
    public AlertScheduler(AlertaVencimiento av, AlertaDisponibilidad ad) {
        this.alertaVencimiento = av;
        this.alertaDisponibilidad = ad;
    }

    /**
     * Inicia la ejecución periódica de las alertas, con una frecuencia de una hora.
     * Programa la revisión de vencimientos y disponibilidad de forma continua.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(
                () -> alertaVencimiento.revisarVencimientos(),
                0, 1, TimeUnit.HOURS
        );
        scheduler.scheduleAtFixedRate(
                () -> alertaDisponibilidad.revisarDisponibilidad(),
                0, 1, TimeUnit.HOURS
        );
    }

    /**
     * Detiene el scheduler e interrumpe la ejecución de futuras tareas.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}