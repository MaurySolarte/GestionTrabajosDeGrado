package com.unicauca.proyectogestion.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class DashboardCoordinadorController implements Initializable {

    @FXML
    private AnchorPane achrPnCentral;

    @FXML
    private Label lblNombre;

    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void mostrarMisDatos() throws IOException {
        misDatosController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "misDatos");

        if (controlador != null) {
            controlador.setUsuario(this.usuario);
        }
    }

    @FXML
    private void listarFormatos() throws IOException {
        CoordinadorListarFormatosController controlador
                = Navegacion.cargarEnAnchorPane(achrPnCentral, "CoordinadorListarFormatos");
    }

    public void inicializarUsuario(Usuario usuario){
        setUsuario(usuario);
        cargarUsuario();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

    }

    public AnchorPane getAchrPane(){
        return this.achrPnCentral;
    }

    private void cargarUsuario(){

        lblNombre.setText(usuario.getNombres() +" "+ usuario.getApellidos());

    }

    @FXML
    void eventBtnCerrarSesion(ActionEvent event) {
        Navegacion.cambiarVista("login");
    }

}
