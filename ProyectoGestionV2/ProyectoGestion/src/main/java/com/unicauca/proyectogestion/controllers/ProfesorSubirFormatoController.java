package com.unicauca.proyectogestion.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private TextArea txtObjetivoEspecifico;

    @FXML
    private TextField txtObjetivoGeneral;

    @FXML
    private TextField txtTitulo;

    private ServicioFormatoA servicioFormatoA = null;
    private ServicioUsuario servicioUsuario = null;
    private Usuario usuario = null;

    private File archivoFormato = null;
    private File archivoCarta = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.imgCarta.setVisible(false);
        this.btnCarta.setVisible(false);

        IRepositorioUsuario repositorio = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        servicioUsuario = new ServicioUsuario(repositorio);

        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);

        // Cargar los profesores en los ComboBox
        List<Profesor> profesores = servicioUsuario.listarProfesores();
        cbxDirector.setItems(FXCollections.observableArrayList(profesores));
        cbxCodirector.setItems(FXCollections.observableArrayList(profesores));
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
        if (rdBtnPI.isSelected() && archivoFormato != null) {
            System.out.println("Archivo seleccionado: " + archivoFormato.getAbsolutePath());
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, null, "formato_a", capturarDatosFormato());
        } else if (rdBtnPP.isSelected() && archivoFormato != null && archivoCarta != null) {
            System.out.println("Archivo seleccionado: " + archivoFormato.getAbsolutePath());
            System.out.println("Archivo seleccionado: " + archivoCarta.getAbsolutePath());
            servicioFormatoA.guardarArchivoEnBD(archivoFormato, archivoCarta, "carta_empresa", capturarDatosFormato());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
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
        String objetivoGeneral = txtObjetivoGeneral.getText();
        String objetivosEspecificos = txtObjetivoEspecifico.getText();

        // Profesor que sube el formato es el usuario logueado
        Profesor profesor = (Profesor)this.usuario;

        return new FormatoA(titulo, modalidad, fecha, director, codirector, estudiante, profesor, objetivoGeneral, objetivosEspecificos);
    }

    @FXML
    void eventClickRdBtnPI(ActionEvent event) {
        if (rdBtnPI.isSelected()) {
            this.rdBtnPP.setSelected(false);
            this.imgCarta.setVisible(false);
            this.btnCarta.setVisible(false);
        }
    }

    @FXML
    void eventClickRdBtnPP(ActionEvent event) {
        if (rdBtnPP.isSelected()) {
            this.rdBtnPI.setSelected(false);
            this.imgCarta.setVisible(true);
            this.btnCarta.setVisible(true);
        }
    }
}
