package net.teujaem.nrDonation.common.event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> listeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void callEvent(String message) {
        for (EventListener listener : listeners) {
            listener.onEvent(message);
        }
    }
}
