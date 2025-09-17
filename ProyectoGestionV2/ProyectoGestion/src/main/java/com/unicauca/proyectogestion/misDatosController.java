package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.domain.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class misDatosController implements Initializable {
    
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
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void cargarUsuario(){
        txtNombres.setText(usuario.getNombres());
        txtApellidos.setText(usuario.getApellidos());
        txtCelular.setText(usuario.getCelular());
        txtEmail.setText(usuario.getEmail());
        txtPrograma.setText(String.valueOf(usuario.getPrograma()));
        txtRol.setText(String.valueOf(usuario.getRol()));
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarUsuario();
    }
    
}
