
package com.unicauca.proyectogestion.service;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author angma
 */
public class ServicioNotificacionesTest {

    @Test
    public void testGetInstance_NoEsNull() {
        ServicioNotificaciones instance = ServicioNotificaciones.getInstance();
        assertNotNull(instance, "getInstance() no debe devolver null");
    }
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ServicioNotificaciones result = ServicioNotificaciones.getInstance();
        assertNotNull(result, "getInstance() no debe devolver null");
        // Verifica que siempre devuelve la misma instancia (singleton)
        ServicioNotificaciones result2 = ServicioNotificaciones.getInstance();
        assertSame(result, result2, "getInstance() debe devolver siempre la misma instancia");
    }

    @Test
    public void testSubscribe() {
        System.out.println("subscribe");

        ServicioNotificaciones instance = ServicioNotificaciones.getInstance();

        final boolean[] fueNotificado = {false};

        Consumer<String> listener = mensaje -> fueNotificado[0] = true;

        instance.subscribe(listener);
        instance.notifyAllListeners("Mensaje de prueba");

        assertTrue(fueNotificado[0], "El listener suscrito no fue notificado correctamente");
    }

    @Test
    public void testNotifyAllListeners() {
        System.out.println("notifyAllListeners");

        ServicioNotificaciones instance = ServicioNotificaciones.getInstance();

        final boolean[] listener1Notificado = {false};
        final boolean[] listener2Notificado = {false};

        Consumer<String> listener1 = msg -> {
            if ("Mensaje de prueba".equals(msg)) {
                listener1Notificado[0] = true;
            }
        };
        Consumer<String> listener2 = msg -> {
            if ("Mensaje de prueba".equals(msg)) {
                listener2Notificado[0] = true;
            }
        };
        instance.subscribe(listener1);
        instance.subscribe(listener2);
        instance.notifyAllListeners("Mensaje de prueba");
        assertTrue(listener1Notificado[0], "El primer listener no fue notificado");
        assertTrue(listener2Notificado[0], "El segundo listener no fue notificado");
    }


}
