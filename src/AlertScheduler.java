import java.util.concurrent.*;

public class AlertScheduler {
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private AlertaVencimiento alertaVencimiento;
    private AlertaDisponibilidad alertaDisponibilidad;

    public AlertScheduler(AlertaVencimiento av, AlertaDisponibilidad ad) {
        this.alertaVencimiento = av;
        this.alertaDisponibilidad = ad;
    }

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

    public void shutdown() {
        scheduler.shutdown();
    }
}
