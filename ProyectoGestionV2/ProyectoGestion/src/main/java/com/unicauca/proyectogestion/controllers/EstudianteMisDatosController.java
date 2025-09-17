package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.domain.Usuario;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EstudianteMisDatosController implements Initializable {

    @FXML
    private AnchorPane anchrPaneCentral;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCelular;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtPrograma;

    @FXML
    private TextField txtRol;

    private Usuario usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void inicializarUsuario(Usuario usuario){
        setUsuario(usuario);
        cargarUsuario();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

    }

    private void cargarUsuario(){
        txtNombres.setText(usuario.getNombres());
        txtApellidos.setText(usuario.getApellidos());
        txtCelular.setText(usuario.getCelular());
        txtEmail.setText(usuario.getEmail());
        txtPrograma.setText(String.valueOf(usuario.getPrograma()));
        txtRol.setText(String.valueOf(usuario.getRol()));
    }
}
