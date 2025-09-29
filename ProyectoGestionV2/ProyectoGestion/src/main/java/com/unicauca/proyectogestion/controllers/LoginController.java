package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
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

    private ServicioFormatoA serviceFormato;
    private ServicioUsuario serviceUsuario;

    @FXML
    private void evenBtnIngresar(ActionEvent event) throws IOException {
        String correo = txt_usuario.getText();
        String contrasenia = txt_contrasenia.getText();

        int valido = serviceUsuario.iniciarSesion(correo, contrasenia);

        switch (valido) {
            case 1:
                Usuario objUsuario = serviceUsuario.obtenerUsuarioPorEmail(correo);
                String rol = serviceUsuario.obtenerRolUsuario(correo);

                if ("Profesor".equalsIgnoreCase(rol)) {
                    Navegacion.mostrarAlerta("Login exitoso", "Bienvenido " + objUsuario.getNombres(), Alert.AlertType.CONFIRMATION);
                    Navegacion.cambiarVistaNuevaVentana("dashboardProfesor","Panel Profesor");
                    DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
                    controlador.setUsuario(objUsuario);
                } else if ("Estudiante".equalsIgnoreCase(rol)) {
                    Navegacion.mostrarAlerta("Login exitoso", "Bienvenido " + objUsuario.getNombres(), Alert.AlertType.CONFIRMATION);
                    Navegacion.cambiarVistaNuevaVentana("dashboardEstudiante","Panel Estudiante");
                    DashboardEstudianteController controlador = Navegacion.getController("dashboardEstudiante");
                    controlador.inicializarUsuario(objUsuario);
                    controlador.mostrarMisDatos();
                }else if ("Coordinador".equalsIgnoreCase(rol)) {
                    Navegacion.cambiarVistaNuevaVentana("dashboardCoordinador", "Panel Coordinador");
                    DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
                    controlador.inicializarUsuario(objUsuario);
                    controlador.mostrarMisDatos();

                } else {
                    Navegacion.mostrarAlerta("Error", "No se pudo determinar el rol del usuario", Alert.AlertType.ERROR);
                }
                break;

            case 2:
                Navegacion.mostrarAlerta("Error de login", "Por favor llene todos los campos requeridos para iniciar sesion", Alert.AlertType.INFORMATION);
                break;
            default:
                Navegacion.mostrarAlerta("Error de login", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
                break;
        }

    }

    @FXML
    private void evenBtnRegistrarse(javafx.scene.input.MouseEvent event) {
        Navegacion.cambiarVista("register");
        RegisterController controler = Navegacion.getController("register");
        controler.limpiar();
    }

    public void limpiar(){
        txt_contrasenia.clear();
        txt_usuario.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IRepositorioUsuario repositorio = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        serviceUsuario = new ServicioUsuario(repositorio);


    }

}
