package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.Coordinador;
import com.unicauca.proyectogestion.domain.Profesor;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioNotificaciones;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class CoordinadorEvaluarFormatoController {

    @FXML private Button btnAprobar;
    @FXML private Button btnCancelar;
    @FXML private Button btnDescargarFormatoA;
    @FXML private Button btnRechazar;
    @FXML private Button btnSubirCorrecion;

    @FXML private Label lblCorreo1;
    @FXML private Label lblCorreo2;
    @FXML private Label lblCorreoEstudiante1;
    @FXML private Label lblCorreoEstudiante2;
    @FXML private Label lblNombreDirector;
    @FXML private Label lblNombrePropuesta;
    @FXML private Label lblTipoPropuesta;

    private int idFormatoSeleccionado;
    private String tipoFormatoSeleccionado;
    private ServicioFormatoA servicioFormatoA;
    private ServicioUsuario servicioUsuario;
    private FormatoATabla formato;
    private byte[] archivoDevolucion;

    private int idCoordinador;
    private int idProfesor;
    private Usuario cordi ;

    public void setUsuario(Usuario usuario) {
        this.cordi = usuario;
    }

    @FXML
    public void initialize() {
        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);
        IRepositorioUsuario repositorioUsuario = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        servicioUsuario = new ServicioUsuario(repositorioUsuario);

    }

    public void setIdFormato(String idFormato, String tipoProyecto) {
        this.idFormatoSeleccionado = Integer.parseInt(idFormato);
        this.tipoFormatoSeleccionado = tipoProyecto;
        cargarDatosFormato();
    }

    public void cargarDatosFormato() {
        formato = servicioFormatoA.obtenerFormato(idFormatoSeleccionado);
        if (formato != null) {
            lblNombrePropuesta.setText(formato.getTitulo());
            lblNombreDirector.setText(formato.getDirector());
            lblTipoPropuesta.setText(formato.getTipoProyecto());

            this.idProfesor = servicioFormatoA.obtenerIdProfesorPorFormato(idFormatoSeleccionado, tipoFormatoSeleccionado);

            String correo1 = formato.getCorreoEstudiante1();
            String correo2 = formato.getCorreoEstudiante2();

            lblCorreoEstudiante1.setVisible(true);
            lblCorreo1.setVisible(true);
            lblCorreo1.setText(correo1);

            if (correo2 != null && !correo2.isEmpty()) {
                lblCorreoEstudiante2.setVisible(true);
                lblCorreo2.setVisible(true);
                lblCorreo2.setText(correo2);
            } else {
                lblCorreoEstudiante2.setVisible(false);
                lblCorreo2.setVisible(false);
            }
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
    void eventBtnAprobar(ActionEvent event) {
        ServicioNotificaciones.getInstance().notifyAllListeners(
                " Se ha evaluado el formato de investigación."
        );
        guardarDevolucionYActualizarEstado("Aprobado");
    }

    @FXML
    void eventBtnRechazar(ActionEvent event) {
        ServicioNotificaciones.getInstance().notifyAllListeners(
                " Se ha evaluado el formato de investigación."
        );
        guardarDevolucionYActualizarEstado("Rechazado");
    }

    private void guardarDevolucionYActualizarEstado(String estado) {
        if (archivoDevolucion == null) {
            mostrarAlerta("Advertencia", "Debe subir un archivo de devolución antes de " + estado.toLowerCase() + ".");
            return;
        }
        int idCoordinador = this.cordi.getIdUsuario();

        servicioFormatoA.registrarDevolucionFormatoA(
                idFormatoSeleccionado,
                idProfesor,
                idCoordinador,
                lblCorreo1.getText(),
                lblCorreo2.isVisible() ? lblCorreo2.getText() : null,
                archivoDevolucion,
                lblTipoPropuesta.getText(),
                obtenerIntento(idFormatoSeleccionado)
        );


        servicioFormatoA.actualizarEstadoFormato(idFormatoSeleccionado,lblTipoPropuesta.getText(), estado);
        // Actualizar estado en los estudiantes (por correo)
        if (lblCorreo1.getText() != null && !lblCorreo1.getText().isEmpty()) {
            servicioUsuario.actualizarEstadoEstudiantePorCorreo(lblCorreo1.getText(), estado);
        }

        if (lblCorreo2.isVisible() && lblCorreo2.getText() != null && !lblCorreo2.getText().isEmpty()) {
            servicioUsuario.actualizarEstadoEstudiantePorCorreo(lblCorreo2.getText(), estado);
        }

        mostrarAlerta("Éxito", "Formato " + estado + " y devolución registrada.");
        archivoDevolucion = null;

        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        CoordinadorListarFormatosController ctrl = Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorListarFormatos");
        ctrl.setUsuario(this.cordi);
    }

    private int obtenerIntento(int idFormato) {
        return servicioFormatoA.obtenerNumeroDeIntentos(idFormato);
    }

    @FXML
    void eventBtnSubirCorreccion(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de devolución");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                archivoDevolucion = Files.readAllBytes(file.toPath());
                System.out.println("Archivo cargado en memoria: " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                Navegacion.mostrarAlerta("Error", "No se pudo cargar el archivo de devolución.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void eventBtnCancelar(ActionEvent event) {
        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorListarFormatos");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Navegacion.mostrarAlerta(titulo, mensaje, Alert.AlertType.INFORMATION);
    }
}
