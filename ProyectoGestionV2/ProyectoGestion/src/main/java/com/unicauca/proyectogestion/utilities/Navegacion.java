package com.unicauca.proyectogestion.utilities;

import com.unicauca.proyectogestion.RegisterController;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

public class Navegacion{
    private static final Map<String, Parent> vistas = new HashMap<>();
    private static Stage stage;

    // Inicializa el stage principal desde App
    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    // Carga y guarda las vistas solo la primera vez
    private static Parent loadFXML(String fxml) throws IOException {
        if (!vistas.containsKey(fxml)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Navegacion.class.getResource("/com/unicauca/proyectogestion/" + fxml + ".fxml"));
            
            vistas.put(fxml, fxmlLoader.load());
        }
        return vistas.get(fxml);
    }

    // Cambia de escena
    public static void cambiarVista(String nombre) {
        try {
            Parent root = loadFXML(nombre);
            if (stage.getScene() == null) {
                stage.setScene(new Scene(root));
            } else {
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
