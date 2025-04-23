import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AlertHistory {
    private static final List<NotificacionEntry> history = Collections.synchronizedList(new ArrayList<>());

    public static void addEntry(NotificacionEntry entry) {
        history.add(entry);
    }

    public static List<NotificacionEntry> getHistory() {
        return new ArrayList<>(history);
    }
}