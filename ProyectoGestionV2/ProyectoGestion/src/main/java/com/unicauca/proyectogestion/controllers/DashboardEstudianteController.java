/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class DashboardEstudianteController implements Initializable {

    @FXML
    private AnchorPane anchrPaneCentral;

    @FXML
    private Button btn_cerrarSesion;

    @FXML
    private HBox hboxMiProyecto;

    @FXML
    private HBox hboxMisDatos;

    @FXML
    private Label lblNombre;
    
    private Usuario usuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void inicializarUsuario(Usuario usuario){
        setUsuario(usuario);
        cargarUsuario();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

    }
    
    private void cargarUsuario(){

        lblNombre.setText(usuario.getNombres() +" "+ usuario.getApellidos());

    }

    @FXML
    private void eventBtnCerrarSesion(ActionEvent event) {
        Navegacion.cambiarVista("login");
    }

    @FXML
    void mostrarMisDatos() {
        misDatosController controlador = Navegacion.cargarEnAnchorPane(anchrPaneCentral, "misDatos");
        controlador.inicializarUsuario(usuario);
    }

    @FXML
    void mostrarMiProyecto(MouseEvent event) {
        EstudianteSeguimientoProyectoController controlador =
                Navegacion.cargarEnAnchorPane(anchrPaneCentral,"EstudianteSeguimientoProyecto");


    }
    
}
