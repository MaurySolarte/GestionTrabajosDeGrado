/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class DashboardProfesorController implements Initializable {

    @FXML
    private AnchorPane achrPnCentral;

    @FXML
    private Button btn_cerrarSesion;

    @FXML
    private Label lblNombre;

    private Usuario usuario;

    @FXML
    private void mostrarMisDatos() throws IOException {
        ProfesorMisDatosController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "ProfesorMisDatos");

        if (controlador != null) {
            controlador.setUsuario(this.usuario);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void cargarUsuario() {
        lblNombre.setText(usuario.getNombres() + " " + usuario.getApellidos());
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarUsuario();
    }

    @FXML
    void eventBtnCerrarSesion(ActionEvent event) {
        Navegacion.cambiarVista("login");
    }

}
