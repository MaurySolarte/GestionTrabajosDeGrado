package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;

public class CoordinadorEvaluarFormatoController  {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnDescargarFormatoA;

    @FXML
    private Button btnEnviarEvalucacion;

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
    private int idFormatoSeleccionado;
    private String tipoFormatoSeleccionado; // "investigacion" o "practica"
    private ServicioFormatoA servicioFormatoA;
    private FormatoATabla formato;

    @FXML
    public void initialize() {
        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);

    }

    @FXML
    void eventBtnCancelar(ActionEvent event) {

    }
    public void setIdFormato(String idFormato, String tipoProyecto) {
        this.idFormatoSeleccionado = Integer.parseInt(idFormato);
        this.tipoFormatoSeleccionado = tipoProyecto;
    }

    public void cargarDatosFormato() {
        formato = servicioFormatoA.obtenerFormato(idFormatoSeleccionado);
        lblNombreDirector.setText("adasdasdasdad");
        if (formato != null) {
            lblCorreoEstudiante.setText(formato.getCorreoEstudiante());
            lblNombreDirector.setText(formato.getDirector());
            lblNombrePropuesta.setText(formato.getTitulo());
            lblTipoPropuesta.setText(formato.getTipoProyecto());
        } else {
            System.out.println("No se encontró el formato con ID: " + idFormatoSeleccionado);
        }
    }

    @FXML
    void eventBtnDescargarFormatoA(ActionEvent event) {
        byte[] archivo = servicioFormatoA.obtenerArchivoFormatoA(idFormatoSeleccionado, tipoFormatoSeleccionado);

        if (archivo != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Formato A");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(btnDescargarFormatoA.getScene().getWindow());

                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(archivo);
                        fos.flush();
                    }
                    System.out.println("Archivo descargado correctamente en: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el archivo en la BD.");
        }
    }

    @FXML
    void eventBtnEnviarEvaluacion(ActionEvent event) {

    }

}
