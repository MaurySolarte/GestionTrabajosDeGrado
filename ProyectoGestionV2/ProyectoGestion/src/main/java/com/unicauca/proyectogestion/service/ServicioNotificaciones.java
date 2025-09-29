package com.unicauca.proyectogestion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServicioNotificaciones {
    private static ServicioNotificaciones instance;
    private final List<Consumer<String>> listeners = new ArrayList<>();

    private ServicioNotificaciones() {}

    public static ServicioNotificaciones getInstance() {
        if (instance == null) {
            instance = new ServicioNotificaciones();        }
        return instance;
    }

    public void subscribe(Consumer<String> listener) {
        listeners.add(listener);
    }

    public void notifyAllListeners(String message) {
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
        System.out.println("[NOTIFICACIÓN SIMULADA] " + message);
    }
}
