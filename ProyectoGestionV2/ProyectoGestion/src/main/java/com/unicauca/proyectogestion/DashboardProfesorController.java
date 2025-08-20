package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.*;
import com.unicauca.proyectogestion.domain.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DashboardProfesorController implements Initializable {
    
    @FXML
    private Label lblNombre;
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
    
    public void cargarUsuario(){
        txtNombres.setText(usuario.getNombres());
        lblNombre.setText(usuario.getNombres() +" "+ usuario.getApellidos());
        txtApellidos.setText(usuario.getApellidos());
        txtCelular.setText(String.valueOf(usuario.getCelular()));
        txtEmail.setText(usuario.getEmail());
        txtPrograma.setText(String.valueOf(usuario.getPrograma()));
        txtRol.setText(String.valueOf(usuario.getRol()));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarUsuario();
    }
    
    
    
}
