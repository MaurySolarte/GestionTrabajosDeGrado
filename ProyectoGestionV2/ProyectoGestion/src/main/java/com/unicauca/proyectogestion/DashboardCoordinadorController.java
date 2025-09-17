package com.unicauca.proyectogestion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;


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
    private void mostrarMisDatos() throws IOException {
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
}
