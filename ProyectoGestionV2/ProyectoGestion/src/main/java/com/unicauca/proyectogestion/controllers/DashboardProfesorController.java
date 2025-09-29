/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.domain.Profesor;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.ServicioNotificaciones;
import com.unicauca.proyectogestion.utilities.Navegacion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DashboardProfesorController implements Initializable {

    @FXML
    private AnchorPane achrPnCentral;

    @FXML
    private Label lblNombre;

    private Usuario usuario;

    @FXML
    private void mostrarMisDatos() throws IOException {
        misDatosController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "misDatos");


        if (controlador != null) {
            controlador.setUsuario(this.usuario);
        }
    }
    
    @FXML
    private void mostrarSubirFormato() {
        ProfesorSubirFormatoController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "ProfesorSubirFormato");
        controlador.setUsuario(usuario);
                
    }

    @FXML
    private void mostrarResubirFormato() {
        ProfesorCorreccionesController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "ProfesorCorrecciones");
        controlador.setUsuario(usuario);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ServicioNotificaciones.getInstance().subscribe(msg -> {
            Platform.runLater(() -> {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Nueva Notificación");
                alerta.setHeaderText("Actualización del sistema");
                alerta.setContentText(msg);
                alerta.show();
            });
        });
        
    }

    public void cargarUsuario() {
        lblNombre.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    public void setUsuario(Usuario usuario) throws IOException {
        this.usuario = usuario;
        cargarUsuario();
        mostrarMisDatos();
    }

    @FXML
    void eventBtnCerrarSesion(ActionEvent event) {
        Navegacion.cambiarVista("login");
    }

    public AnchorPane getAchrPane(){
        return this.achrPnCentral;
    }
}
