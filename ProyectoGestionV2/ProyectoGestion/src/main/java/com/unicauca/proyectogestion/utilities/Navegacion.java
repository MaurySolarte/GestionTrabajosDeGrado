package com.unicauca.proyectogestion.utilities;

import com.unicauca.proyectogestion.App;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Navegacion {

    private static final Map<String, Parent> vistas = new HashMap<>();
    private static final Map<String, Object> controladores = new HashMap<>();
    private static Stage stage;

    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        if (!vistas.containsKey(fxml)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Navegacion.class.getResource(
                    "/com/unicauca/proyectogestion/" + fxml + ".fxml"
            ));
            Parent root = fxmlLoader.load();
            vistas.put(fxml, root);
            controladores.put(fxml, fxmlLoader.getController());
        }
        return vistas.get(fxml);
    }

    public static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        // Cambiar título e ícono de ventana
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        // Crear un Label personalizado para el mensaje
        Label etiqueta = new Label(mensaje);
        etiqueta.setWrapText(true);
        etiqueta.setStyle("-fx-font-Tebuchet: MS 14px; -fx-font-family: 'Segoe UI'; -fx-text-fill: #2c3e50;");

        // Meter el Label en un contenedor para darle padding
        VBox contenedor = new VBox(etiqueta);
        contenedor.setSpacing(10);
        contenedor.setPadding(new Insets(10));

        alerta.getDialogPane().setContent(contenedor);

        // Aplicar estilo al cuadro de diálogo completo
        alerta.getDialogPane().setStyle(
                "-fx-background-color: #f9f9f9; " +
                        "-fx-border-color: #ABBEF6; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        alerta.getDialogPane().lookupButton(ButtonType.OK)
                .setStyle("-fx-background-color: #1E2C9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7px;");

        alerta.showAndWait();
    }

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

    public static void cambiarVistaNuevaVentana(String nombreVista, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource("/com/unicauca/proyectogestion/" + nombreVista + ".fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();

            // Guardar el controlador para luego recuperarlo
            controladores.put(nombreVista, loader.getController());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static <T> T cargarEnAnchorPane(AnchorPane contenedor, String nombre) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Navegacion.class.getResource(
                    "/com/unicauca/proyectogestion/" + nombre + ".fxml"
            ));
            Node nodo = fxmlLoader.load();

            // Reemplazar contenido
            contenedor.getChildren().setAll(nodo);

            // Retornar el controlador
            T controlador = fxmlLoader.getController();
            controladores.put(nombre, controlador); // opcional, si quieres guardarlo
            return controlador;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T getController(String nombre) {
        return (T) controladores.get(nombre);
    }
}
