package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;

public class CoordinadorEvaluarFormatoController  {

    @FXML
    private Button btnCancelar;

    @FXML
    private RadioButton rdbAdjuntarFirma;

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

    public void setIdFormato(String idFormato, String tipoProyecto) {
        this.idFormatoSeleccionado = Integer.parseInt(idFormato);
        this.tipoFormatoSeleccionado = tipoProyecto;
        cargarDatosFormato();
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
        String estado = rdbAdjuntarFirma.isSelected() ? "Aprobado" : "Rechazado";
        String mensaje = estado.equals("Aprobado")
                ? "¿Está seguro de aprobar este Formato A?"
                : "¿Está seguro de rechazar este Formato A?";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnAceptar, btnCancelar);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnAceptar) {
                boolean exito = servicioFormatoA.actualizarEstadoFormato(idFormatoSeleccionado, tipoFormatoSeleccionado, estado);
                if (exito) {
                    Navegacion.mostrarAlerta("Éxito", "El Formato A fue " + estado + " correctamente.", Alert.AlertType.INFORMATION);
                } else {
                    Navegacion.mostrarAlerta("Error", "No se pudo actualizar el estado.", Alert.AlertType.ERROR);
                }
            }
        });

        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorListarFormatos");

    }

    @FXML
    void eventBtnCancelar(ActionEvent event) {
        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorListarFormatos");
    }

}
