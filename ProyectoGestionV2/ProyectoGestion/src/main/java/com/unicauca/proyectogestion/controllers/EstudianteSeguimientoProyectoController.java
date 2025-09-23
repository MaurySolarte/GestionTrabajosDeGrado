package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class EstudianteSeguimientoProyectoController implements Initializable {

    @FXML
    private Label lblEstadoActual;

    @FXML
    private Label lblFechaInicio;

    @FXML
    private Label lblNombreEstudiante;

    @FXML
    private Label lblTipoProyecto;

    @FXML
    private Label lblTutor;

    @FXML
    private Tab tbpnHistorial;

    @FXML
    private Tab tbpnInfoProyecto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
