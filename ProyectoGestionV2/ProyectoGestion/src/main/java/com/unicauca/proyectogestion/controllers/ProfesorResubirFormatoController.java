package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

public class ProfesorResubirFormatoController {

    @FXML private Button btnCancelar, btnFormato, btnSubir;
    @FXML private DatePicker dtPckrFecha;
    @FXML private Label lblDirector, lblCodirector, lblModalidad, lblIntento;
    @FXML private Label lblCorreoEstudiante1, lblCorreoEstudiante2;
    @FXML private TextField txtTitulo, txtObjetivoGeneral;
    @FXML private TextArea txtObjetivoEspecifico;
    @FXML private VBox vBoxEstudiante2;

    private ServicioFormatoA servicioFormatoA;
    private ServicioUsuario servicioUsuario;
    private Usuario usuario;               // profesor logueado
    private FormatoATabla formatoOriginal; // info simple de la BD
    private File archivoFormato = null;           // nuevo archivo proyecto
    private byte[] archivoCarta ;
    private int intento;// nueva carta (solo en práctica)
    private String modalidad;

    @FXML
    public void initialize() {
        IRepositorioUsuario repoUsuario = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        servicioUsuario = new ServicioUsuario(repoUsuario);
        IRepositorioFormatoA repoFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repoFormatoA);

    }

    @FXML
    void eventClickBtnCancelar(MouseEvent event) {

        DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
        ProfesorCorreccionesController ctrl = Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
        ctrl.setUsuario(usuario);
    }

    public void setDatos(Usuario usuario, int idFormato, String modalidad) {
        this.usuario = usuario;
        this.modalidad = modalidad;
        this.formatoOriginal = servicioFormatoA.obtenerFormatoCompleto(idFormato,modalidad);

        if (formatoOriginal == null) {
            //mostrarAlerta("No se encontró el Formato A con id " + idFormato);
            return;
        }

        lblDirector.setText(formatoOriginal.getDirector());
        lblCodirector.setText(formatoOriginal.getCodirector());
        lblModalidad.setText(formatoOriginal.getTipoProyecto());
        lblIntento.setText(String.valueOf(Integer.parseInt(formatoOriginal.getIntentoActual())+1));
        lblCorreoEstudiante1.setText(formatoOriginal.getCorreoEstudiante1());
        lblCorreoEstudiante2.setText(
                formatoOriginal.getCorreoEstudiante2() != null ? formatoOriginal.getCorreoEstudiante2() : "N/A"
        );
        vBoxEstudiante2.setVisible(formatoOriginal.getCorreoEstudiante2() != null);
        archivoCarta = formatoOriginal.getCartaEmpresa();
        txtTitulo.setText(formatoOriginal.getTitulo());
        txtObjetivoGeneral.setText(formatoOriginal.getObjetivoGeneral());
        txtObjetivoEspecifico.setText(formatoOriginal.getObjetivosEspecificos());
        dtPckrFecha.setValue(LocalDate.parse(formatoOriginal.getFechaInicio()));
        intento = Integer.parseInt(formatoOriginal.getIntentoActual())+1;

    }

    @FXML
    void eventClickBtnFormato() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Formato A (PDF)");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        archivoFormato = fc.showOpenDialog(btnFormato.getScene().getWindow());
    }

    @FXML
    void eventClickBtnSubir(MouseEvent event) {

        FormatoA formato = capturarDatosFormato();

        if (formato == null) {
            System.out.println("Error al capturar los datos del formato.");
            return;
        }

        if (archivoFormato == null) {
            Navegacion.mostrarAlerta("Aviso", "Debe seleccionar un archivo PDF del Formato A", Alert.AlertType.WARNING);
            return;
        }


        if ("Investigacion".equalsIgnoreCase(formatoOriginal.getTipoProyecto())) {
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, null, "formato_a", formato);
            Navegacion.mostrarAlerta("Aviso", "Formato A resubido Correctamente", Alert.AlertType.INFORMATION);
            DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
            AnchorPane anchorPaneCentral = controlador.getAchrPane();
            Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
            ProfesorCorreccionesController ctrl = Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
            ctrl.setUsuario(usuario);
        } else {
            File archivoCarta1 = convertirBytesAFile(archivoCarta, "carta_empresa.pdf");
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, archivoCarta1, "carta_empresa", formato);
            Navegacion.mostrarAlerta("Aviso", "Formato A resubido Correctamente", Alert.AlertType.INFORMATION);
            DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
            AnchorPane anchorPaneCentral = controlador.getAchrPane();
            Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
            ProfesorCorreccionesController ctrl = Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorCorrecciones");
            ctrl.setUsuario(usuario);
        }

        String nuevoEstado = "";

        if (intento == 2) {
            nuevoEstado = "SegundaRevision";
        } else if (intento == 3) {
            nuevoEstado = "TercerRevision";
        }

        if (!nuevoEstado.isEmpty()) {
            // Estudiante 1
            if (formatoOriginal.getCorreoEstudiante1() != null) {
                servicioUsuario.actualizarEstadoEstudiantePorCorreo(formatoOriginal.getCorreoEstudiante1(), nuevoEstado);
            }
            // Estudiante 2
            if (formatoOriginal.getCorreoEstudiante2() != null) {
                servicioUsuario.actualizarEstadoEstudiantePorCorreo(formatoOriginal.getCorreoEstudiante2(), nuevoEstado);
            }
        }

    }

    private File convertirBytesAFile(byte[] data, String nombreArchivo) {
        if (data == null) {
            return null; // si no existe archivo guardado
        }

        File archivoTemporal = new File(System.getProperty("java.io.tmpdir"), nombreArchivo);
        try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return archivoTemporal;
    }

    private FormatoA capturarDatosFormato() {
        String titulo = txtTitulo.getText();
        LocalDate fecha = dtPckrFecha.getValue();
        String objetivoGeneral = txtObjetivoGeneral.getText();
        String objetivosEspecificos = txtObjetivoEspecifico.getText();

        Profesor director = servicioUsuario.obtenerProfesorPorNombre(lblDirector.getText());
        Profesor codirector = servicioUsuario.obtenerProfesorPorNombre(lblCodirector.getText());
        Estudiante estudiante1 = servicioUsuario.obtenerEstudiantePorCorreo(lblCorreoEstudiante1.getText());
        Estudiante estudiante2 = (formatoOriginal.getCorreoEstudiante2() != null)
                ? servicioUsuario.obtenerEstudiantePorCorreo(formatoOriginal.getCorreoEstudiante2())
                : null;

        Profesor profesor = (Profesor) usuario;

        if (titulo.isBlank() || fecha == null || objetivoGeneral.isBlank() || objetivosEspecificos.isBlank()) {
            System.out.println("Faltan datos obligatorios.");
            return null;
        }

        if ("Investigacion".equalsIgnoreCase(formatoOriginal.getTipoProyecto())) {
            return new FormatoAInvestigacion(
                    titulo, EnumModalidad.Investigacion, fecha,
                    director, codirector, estudiante1, estudiante2, profesor,
                    objetivoGeneral, objetivosEspecificos
            );
        } else {
            return new FormatoAPracticaProfesional(
                    titulo, EnumModalidad.PracticaProfesional, fecha,
                    director, codirector, estudiante1, profesor,
                    objetivoGeneral, objetivosEspecificos, null
            );
        }
    }

}
