
package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.*;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class RegisterController implements Initializable {

    @FXML
    private ComboBox<String> cbxPrograma;

    @FXML
    private RadioButton rdbEstudiante;

    @FXML
    private RadioButton rdbDocente;

    @FXML
    private RadioButton rdbCoordinador;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCelular;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombres;
    
    @FXML
    private Button btnCrearCuenta;
    

    //Variables globales de objetos que se usan en toda la clase.
    //Factory gestion = new Factory();
    //IRepositorioUsuario repositorio = null;
    private Usuario nuevoUsuario = null;
    
    private ServicioUsuario servicioUsuario = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbxPrograma.setItems(
        FXCollections.observableArrayList(
            "Ingeniería de Sistemas",
            "Ingeniería electrónica y de telecomunicaciones",
            "Automática industrial",
            "Tecnología en telemática"
            )                               
        );        
        
        txtCelular.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    txtCelular.setText(newValue.replaceAll("[^\\d]", "")); 
                }
            }
        );                       
        
        IRepositorioUsuario repositorio = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        servicioUsuario = new ServicioUsuario(repositorio);
        
    }  

    @FXML
    private void eventClickbtnCrearCuenta(ActionEvent event) {
                        
        if(validarCamposVacios() == false){
            capturarDatosUsuario();
            //if(validarContrasenia() && validarCorreo()){
            registrarUsuario();
            //}
        }        
                
    }
    
    private boolean validarCamposVacios(){        
        
        if(txtNombres.getText() == ""){                        
            Navegacion.mostrarAlerta("Campos vacíos", "Por favor ingrese sus nombres.", Alert.AlertType.WARNING);
            return true;
        }else if(txtApellidos.getText() == ""){
            Navegacion.mostrarAlerta("Campos vacíos", "Por favor ingrese sus apellidos.", Alert.AlertType.WARNING);
            return true;
        }else if(txtCorreo.getText() == ""){
            Navegacion. mostrarAlerta("Campos vacíos", "Por favor ingrese un correo.", Alert.AlertType.WARNING);
            return true;
        }else if(txtContrasenia.getText() == ""){
            Navegacion.mostrarAlerta("Campos vacíos", "Por favor ingrese una contraseña.", Alert.AlertType.WARNING);
            return true;
        }else if(cbxPrograma.getValue() == null){
            Navegacion.mostrarAlerta("Campos vacíos", "Por favor seleccione el programa al que pertenece.", Alert.AlertType.WARNING);
            return true;
        }else if(rdbDocente.isSelected() == false && rdbEstudiante.isSelected() == false && rdbCoordinador.isSelected() == false){
            Navegacion.mostrarAlerta("Por favor seleccione un rol.", "Campos vacíos", Alert.AlertType.WARNING);
            return true;
        }      
        return false;
        
    }
    
    private void capturarDatosUsuario(){
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String correo = txtCorreo.getText();
        
        String contrasenia = txtContrasenia.getText();
        String celular = txtCelular.getText();
        EnumProgramas programa = null;
        switch(cbxPrograma.getValue()){
            case "Ingeniería de Sistemas":
                programa = EnumProgramas.Ingeniería_de_Sistemas;                
                break;
            case "Ingeniería electrónica y de telecomunicaciones":
                programa = EnumProgramas.Ingeniería_Electrónica_y_Telecomunicaciones;                
            case "Automática industrial":
                programa = EnumProgramas.Automática_industrial;                
            case "Tecnología en telemática":
                programa = EnumProgramas.Tecnología_en_Telemática;                
        }
        EnumRoles rol;
        if(rdbDocente.isSelected()){
            rol = EnumRoles.Profesor;
        }else if (rdbEstudiante.isSelected()){
            rol = EnumRoles.Estudiante;
        }else{
            rol = EnumRoles.Coordinador;
        }
        
        nuevoUsuario = new Usuario(nombres, apellidos, celular, programa, rol, correo, contrasenia);
        
    }
    
    private void registrarUsuario(){
        try{        
            if(servicioUsuario.registrarUsuario(nuevoUsuario)){
                Navegacion.mostrarAlerta("Cuenta creada", "Cuenta creada exitosamente", Alert.AlertType.INFORMATION);

            }
            else{
                Navegacion.mostrarAlerta("Cuenta existente.", "Ya existe una cuenta registrada con ese correo.", Alert.AlertType.ERROR);
            }
        }catch(SQLException ex){
            Navegacion.mostrarAlerta("Error al crear cuenta.", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }    
    
    private boolean validarContrasenia(){
        if(servicioUsuario.validarContrasenaSegura(nuevoUsuario.getContrasenia()) == "OK"){
            return true;
        }
        else{
            Navegacion.mostrarAlerta("Contraseña Incorrecta", servicioUsuario.validarContrasenaSegura(nuevoUsuario.getContrasenia()), Alert.AlertType.ERROR);
            return false;
        }
    }
    
    private boolean validarCorreo(){
        if(servicioUsuario.validarCorreoInstitucional(nuevoUsuario.getEmail()) == "OK"){
            return true;
        }
        else{
            Navegacion.mostrarAlerta("Contraseña Incorrecta", servicioUsuario.validarCorreoInstitucional(nuevoUsuario.getEmail()), Alert.AlertType.ERROR);
            return false;
        }
    }

    @FXML
    private void eventClickcrdbEstudiante(ActionEvent event) {
        if(rdbEstudiante.isSelected()){
            this.rdbDocente.setSelected(false);
            this.rdbCoordinador.setSelected(false);
        }
    }
    
    @FXML
    private void eventClickcrdbDocente(ActionEvent event) {
        if(rdbDocente.isSelected()){
            this.rdbEstudiante.setSelected(false);
            this.rdbCoordinador.setSelected(false);
        }
    }

    @FXML
    private void eventClickcrdbCoordinador(ActionEvent event) {
        if(rdbCoordinador.isSelected()){
            this.rdbEstudiante.setSelected(false);
            this.rdbDocente.setSelected(false);
        }
    }
     
     @FXML
    void eventClicklblVolver(MouseEvent event) {
         Navegacion.cambiarVista("login");
    }
    
}
