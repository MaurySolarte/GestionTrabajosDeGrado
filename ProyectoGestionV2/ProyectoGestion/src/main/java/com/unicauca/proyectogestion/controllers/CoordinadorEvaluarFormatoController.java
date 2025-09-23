package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.utilities.Navegacion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class CoordinadorEvaluarFormatoController implements Initializable {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEnviarEvalucacion;

    @FXML
    private CheckBox checkFirma;

    @FXML
    private Label lblCorreoEstudiante;

    @FXML
    private Label lblNombreDirector;

    @FXML
    private Label lblNombrePropuesta;

    @FXML
    private Label lblTipoPropuesta;

    @FXML
    private TextArea txtComentarios;

    @FXML
    void eventBtnCancelar(ActionEvent event) {
        Navegacion.cambiarVista("CoordinadorListarformatos");
    }

    @FXML
    void eventBtnEnviarEvaluacion(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
