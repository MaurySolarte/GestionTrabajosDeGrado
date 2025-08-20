/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.*;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.Navegacion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DashboardEstudianteController implements Initializable {

    @FXML
    private Label lblNombre;
    @FXML
    private Button btn_cerrarSesion;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtRol;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtPrograma;
    @FXML
    private TextField txtEmail;
    
    private Usuario usuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarUsuario();
    }
    
    private void cargarUsuario(){
        txtNombres.setText(usuario.getNombres());
        lblNombre.setText(usuario.getNombres() +" "+ usuario.getApellidos());
        txtApellidos.setText(usuario.getApellidos());
        txtCelular.setText(usuario.getCelular());
        txtEmail.setText(usuario.getEmail());
        txtPrograma.setText(String.valueOf(usuario.getPrograma()));
        txtRol.setText(String.valueOf(usuario.getRol()));
    }

    @FXML
    private void eventBtnCerrarSesion(ActionEvent event) {
        Navegacion.cambiarVista("login");
    }
    
}
