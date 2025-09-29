package com.unicauca.proyectogestion.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import com.unicauca.proyectogestion.service.ServicioNotificaciones;
import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProfesorSubirFormatoController implements Initializable {

    @FXML
    private Button btnCarta;

    @FXML
    private Button btnFormato;

    @FXML
    private Button btnSubir;

    @FXML
    private DatePicker dtPckrFecha;

    @FXML
    private ImageView imgCarta;

    @FXML
    private ImageView imgFormato;

    @FXML
    private RadioButton rdBtnPI;

    @FXML
    private RadioButton rdBtnPP;

    @FXML
    private ComboBox<Profesor> cbxDirector;  // Cambio

    @FXML
    private ComboBox<Profesor> cbxCodirector; // Cambio

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtCorreo2;

    @FXML
    private TextArea txtObjetivoEspecifico;

    @FXML
    private TextField txtObjetivoGeneral;

    @FXML
    private TextField txtTitulo;

    @FXML
    private VBox vBoxEstudiante2;


    private ServicioFormatoA servicioFormatoA = null;
    private ServicioUsuario servicioUsuario = null;
    private Usuario usuario = null;

    private File archivoFormato = null;
    private File archivoCarta = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.imgCarta.setVisible(false);
        this.btnCarta.setVisible(false);
        this.vBoxEstudiante2.setVisible(false);

        IRepositorioUsuario repositorio = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        servicioUsuario = new ServicioUsuario(repositorio);

        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);

        // Cargar los profesores en los ComboBox
        List<Profesor> profesores = servicioUsuario.listarProfesores();
        cbxDirector.setItems(FXCollections.observableArrayList(profesores));
        cbxCodirector.setItems(FXCollections.observableArrayList(profesores));

    }

    private boolean puedeSubirProyecto(String correo, EnumModalidad modalidad) {
        Object[] intentoEstadoEstaMod = servicioFormatoA.obtenerUltimoIntentoEstado(correo, modalidad);

        EnumModalidad otraModalidad = (modalidad == EnumModalidad.Investigacion)
                        ? EnumModalidad.PracticaProfesional
                        : EnumModalidad.Investigacion;

        Object[] intentoEstadoOtraMod = servicioFormatoA.obtenerUltimoIntentoEstado(correo, otraModalidad);

        int intentoEsta =0;
        String estadoEsta ="";
        int intentoOtra =0;
        String estadoOtra ="";

        // Parsear valores modalidad actual
        if (intentoEstadoEstaMod != null) {
            if (intentoEstadoEstaMod[0] != null) {
                intentoEsta = Integer.parseInt(intentoEstadoEstaMod[0].toString());
            }
            if (intentoEstadoEstaMod[1] != null) {
                estadoEsta = intentoEstadoEstaMod[1].toString();
            }
        }

        // Parsear valores otra modalidad
        if (intentoEstadoOtraMod != null) {
            if (intentoEstadoOtraMod[0] != null) {
                intentoOtra = Integer.parseInt(intentoEstadoOtraMod[0].toString());
            }
            if (intentoEstadoOtraMod[1] != null) {
                estadoOtra = intentoEstadoOtraMod[1].toString();
            }
        }
        if(intentoEsta ==0 ){
            if(intentoOtra==0 || (intentoOtra==3 && estadoOtra.equalsIgnoreCase("Rechazado"))){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }

    }

    @FXML
    void eventClickBtnFormato() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Formato A (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        archivoFormato = fileChooser.showOpenDialog(btnFormato.getScene().getWindow());
    }

@FXML
    void eventClickBtnCarta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Carta de Recomendación (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        archivoCarta = fileChooser.showOpenDialog(btnCarta.getScene().getWindow());
    }

    @FXML
    void eventClickBtnSubir() {
        FormatoA formato = capturarDatosFormato();
        if (formato == null) {
            System.out.println("Error al capturar los datos del formato.");
            return;
        }

        String correo1 = txtCorreo.getText().trim();
        String correo2 = (rdBtnPI.isSelected() ? txtCorreo2.getText().trim() : null);

        // Validar si los estudiantes pueden iniciar el proceso en la modalidad seleccionada
        if (!puedeSubirProyecto(correo1, formato.getModalidad())) {
            Navegacion.mostrarAlerta("Aviso", "El estudiante con correo " + correo1 + " no puede iniciar un nuevo proyecto en la modalidad seleccionada.", Alert.AlertType.WARNING);
            return;
        }
        if (correo2 != null && !correo2.isBlank() && !puedeSubirProyecto(correo2, formato.getModalidad())) {
            Navegacion.mostrarAlerta("Aviso", "El estudiante con correo " + correo2 + " no puede iniciar un nuevo proyecto en la modalidad seleccionada.", Alert.AlertType.WARNING);
            return;
        }

        if (rdBtnPI.isSelected() && archivoFormato != null) {
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, null, "formato_a", formato);


        } else if (rdBtnPP.isSelected() && archivoFormato != null && archivoCarta != null) {
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, archivoCarta, "carta_empresa", formato);

        } else {
            System.out.println("No se seleccionó ningún archivo.");
            return;
        }

        servicioUsuario.actualizarEstadoEstudiantePorCorreo(correo1,"PrimerRevision");
        if(correo2 != null && !correo2.isBlank()){
            servicioUsuario.actualizarEstadoEstudiantePorCorreo(correo2,"PrimerRevision");}



        limpiarCampos();
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private FormatoA capturarDatosFormato() {
        String titulo = txtTitulo.getText();
        EnumModalidad modalidad = rdBtnPI.isSelected() ? EnumModalidad.Investigacion : EnumModalidad.PracticaProfesional;
        LocalDate fecha = dtPckrFecha.getValue();

        Profesor director = cbxDirector.getValue();
        Profesor codirector = cbxCodirector.getValue();

        Estudiante estudiante = servicioUsuario.obtenerEstudiantePorCorreo(txtCorreo.getText());
        Estudiante estudiante2 = null;
        if (txtCorreo2.getText() != null && !txtCorreo2.getText().isBlank() && rdBtnPI.isSelected()) {
            estudiante2 = servicioUsuario.obtenerEstudiantePorCorreo(txtCorreo2.getText());
        }
        String objetivoGeneral = txtObjetivoGeneral.getText();
        String objetivosEspecificos = txtObjetivoEspecifico.getText();

        if (titulo.isBlank() || fecha == null || director == null || estudiante == null || objetivoGeneral.isBlank() || objetivosEspecificos.isBlank()) {
            System.out.println("Faltan campos obligatorios por llenar.");
            return null;
        }

        // Profesor que sube el formato es el usuario logueado
        Profesor profesor = (Profesor)this.usuario;
        if(rdBtnPI.isSelected()){
            return new FormatoAInvestigacion(titulo, modalidad, fecha, director, codirector, estudiante, estudiante2, profesor, objetivoGeneral, objetivosEspecificos);
        }else if(rdBtnPP.isSelected()){
            return new FormatoAPracticaProfesional(titulo, modalidad, fecha, director, codirector, estudiante, profesor, objetivoGeneral, objetivosEspecificos,null);
        }

        return null;
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtCorreo.setText("");
        txtCorreo2.setText("");
        rdBtnPI.setSelected(false);
        rdBtnPP.setSelected(false);
        dtPckrFecha.setValue(null);
        cbxDirector.setValue(null);
        cbxCodirector.setValue(null);
        txtObjetivoGeneral.setText("");
        txtObjetivoEspecifico.setText("");
        imgCarta.setVisible(false);
        btnCarta.setVisible(false);
        vBoxEstudiante2.setVisible(false);
        archivoFormato = null;
        archivoCarta = null;

    }

    @FXML
    void eventClickRdBtnPI(ActionEvent event) {
        if (rdBtnPI.isSelected()) {
            this.rdBtnPP.setSelected(false);
            this.imgCarta.setVisible(false);
            this.btnCarta.setVisible(false);
            this.vBoxEstudiante2.setVisible(true);
        }else {
            this.vBoxEstudiante2.setVisible(false);
        }
    }

    @FXML
    void eventClickRdBtnPP(ActionEvent event) {
        if (rdBtnPP.isSelected()) {
            this.rdBtnPI.setSelected(false);
            this.imgCarta.setVisible(true);
            this.btnCarta.setVisible(true);
            this.vBoxEstudiante2.setVisible(false);
        }else{
            this.imgCarta.setVisible(false);
            this.btnCarta.setVisible(false);
        }
    }
}
