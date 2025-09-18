package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.Servicio;
import com.unicauca.proyectogestion.utilities.Navegacion;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class LoginController implements Initializable {

    //inicializamos los componentes
    @FXML
    private TextField txt_usuario;
    @FXML
    private PasswordField txt_contrasenia;
    @FXML
    private Button btn_ingresar;
    @FXML
    private Label lbl_registrarse;

    private Servicio service;

    @FXML
    private void evenBtnIngresar(ActionEvent event) throws IOException {
        String correo = txt_usuario.getText();
        String contrasenia = txt_contrasenia.getText();

        int valido = service.iniciarSesion(correo, contrasenia);

        switch (valido) {
            case 1:
                Usuario objUsuario = service.obtenerUsuarioPorEmail(correo);
                String rol = service.obtenerRolUsuario(correo);

                if ("Docente".equalsIgnoreCase(rol)) {
                    mostrarAlerta("Login exitoso", "Bienvenido " + objUsuario.getNombres(), Alert.AlertType.CONFIRMATION);
                    Navegacion.cambiarVista("dashboardCoordinador");
                    //Navegacion.cambiarVista("dashboardProfesor");
                    //DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
                    DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
                    controlador.setUsuario(objUsuario);
                } else if ("Estudiante".equalsIgnoreCase(rol)) {
                    mostrarAlerta("Login exitoso", "Bienvenido " + objUsuario.getNombres(), Alert.AlertType.CONFIRMATION);
                    Navegacion.cambiarVista("dashboardEstudiante");
                    DashboardEstudianteController controlador = Navegacion.getController("dashboardEstudiante");
                    controlador.inicializarUsuario(objUsuario);


                } else {
                    mostrarAlerta("Error", "No se pudo determinar el rol del usuario", Alert.AlertType.ERROR);
                }
                break;

            case 2:
                mostrarAlerta("Error de login", "Por favor llene todos los campos requeridos para iniciar sesion", Alert.AlertType.INFORMATION);
                break;
            default:
                mostrarAlerta("Error de login", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
                break;
        }

    }

    @FXML
    private void evenBtnRegistrarse(javafx.scene.input.MouseEvent event) {
        Navegacion.cambiarVista("register");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        // Cambiar título e ícono de ventana
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        // Crear un Label personalizado para el mensaje
        Label etiqueta = new Label(mensaje);
        etiqueta.setWrapText(true);
        etiqueta.setStyle("-fx-font-Tebuchet MS: 14px; -fx-font-family: 'Segoe UI'; -fx-text-fill: #2c3e50;");

        // Meter el Label en un contenedor para darle padding
        VBox contenedor = new VBox(etiqueta);
        contenedor.setSpacing(10);
        contenedor.setPadding(new Insets(10));

        alerta.getDialogPane().setContent(contenedor);

        // Aplicar estilo al cuadro de diálogo completo
        alerta.getDialogPane().setStyle(
                "-fx-background-color: #f9f9f9; "
                + "-fx-border-color: #ABBEF6; "
                + "-fx-border-width: 3px; "
                + "-fx-border-radius: 5px; "
                + "-fx-background-radius: 5px;"
        );

        // Cambiar estilo de los botones
        alerta.getDialogPane().lookupButton(ButtonType.OK)
                .setStyle("-fx-background-color: #1E2C9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7px;");

        alerta.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IRepositorioUsuario repositorio = Gestion.getInstancia().obtenerRepositorio("SQLite");
        service = new Servicio(repositorio);
    }

}
