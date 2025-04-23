import java.util.concurrent.ConcurrentHashMap;
import java.util.EnumMap;

public class PreferencesManager {
    private ConcurrentHashMap<String, EnumMap<AlertType, Boolean>> prefs = new ConcurrentHashMap<>();

    public void setPreference(String userId, AlertType type, boolean enabled) {
        prefs.computeIfAbsent(userId, k -> {
            EnumMap<AlertType, Boolean> map = new EnumMap<>(AlertType.class);
            for (AlertType t : AlertType.values()) map.put(t, true);
            return map;
        }).put(type, enabled);
    }

    public boolean isEnabled(String userId, AlertType type) {
        return prefs.getOrDefault(userId, new EnumMap<>(AlertType.class)).getOrDefault(type, true);
    }
}
