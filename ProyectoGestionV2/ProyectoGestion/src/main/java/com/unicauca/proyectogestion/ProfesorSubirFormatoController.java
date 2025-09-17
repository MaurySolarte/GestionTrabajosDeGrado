/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.imgCarta.setVisible(false);
        this.btnCarta.setVisible(false);
    }    
    
}
