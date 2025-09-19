/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.EnumModalidad;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author nelso
 */
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
    private TextField txtCodirector;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtDirector;

    @FXML
    private TextArea txtObjetivoEspecifico;

    @FXML
    private TextField txtObjetivoGeneral;

    @FXML
    private TextField txtTitulo;

    private Servicio servicio = null;

    private Usuario usuario = null;

    private File archivoFormato = null;
    private File archivoCarta = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.imgCarta.setVisible(false);
        this.btnCarta.setVisible(false);

        IRepositorioUsuario repositorio = Gestion.getInstancia().obtenerRepositorio("SQLite");
        servicio = new Servicio(repositorio);

    }

    @FXML
    void eventClickBtnFormato() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Formato A (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File file = fileChooser.showOpenDialog(btnFormato.getScene().getWindow());

        archivoFormato = file;

    }

    @FXML
    void eventClickBtnCarta() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Carta de Recomendación (PDF)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File file = fileChooser.showOpenDialog(btnCarta.getScene().getWindow());

        archivoCarta = file;
    }

    @FXML
    void eventClickBtnSubir() {
        if (rdBtnPI.isSelected() && archivoFormato != null) {
            System.out.println("Archivo seleccionado: " + archivoFormato.getAbsolutePath());
            servicio.guardarArchivoEnBD(archivoFormato,null, "formato_a", capturarDatosFormato());
        }else if (rdBtnPP.isSelected() && archivoFormato != null && archivoCarta != null) {
            System.out.println("Archivo seleccionado: " + archivoFormato.getAbsolutePath());
            System.out.println("Archivo seleccionado: " + archivoCarta.getAbsolutePath());
            servicio.guardarArchivoEnBD(archivoFormato, archivoCarta, "carta_empresa", capturarDatosFormato());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }

    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    FormatoA capturarDatosFormato(){
        String titulo = txtTitulo.getText();
        EnumModalidad modalidad = EnumModalidad.valueOf(rdBtnPI.isSelected() ? "Investigacion" : "PracticaProfesional");
        LocalDate fecha = dtPckrFecha.getValue();
        String director = txtDirector.getText();
        String codirector = txtCodirector.getText();
        String correo = txtCorreo.getText();
        String objetivoGeneral = txtObjetivoGeneral.getText();
        String objetivoEspecifico = txtObjetivoEspecifico.getText();

        FormatoA formato = new FormatoA(titulo, modalidad, fecha, director, codirector, correo, usuario.getIdUsuario(), objetivoGeneral, objetivoEspecifico);
        return formato;
    }


    @FXML
    void eventClickRdBtnPI(ActionEvent event) {
        if(rdBtnPI.isSelected()){
            this.rdBtnPP.setSelected(false);
            this.imgCarta.setVisible(false);
            this.btnCarta.setVisible(false);
        }
    }

    @FXML
    void eventClickRdBtnPP(ActionEvent event) {
        if(rdBtnPP.isSelected()){
            this.rdBtnPI.setSelected(false);
            this.imgCarta.setVisible(true);
            this.btnCarta.setVisible(true);
        }
    }

    
}
