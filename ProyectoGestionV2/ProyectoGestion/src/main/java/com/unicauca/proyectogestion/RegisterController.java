
package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class RegisterController implements Initializable {

    @FXML
    private ComboBox<String> cbxPrograma;

    @FXML
    private CheckBox chbxDocente;

    @FXML
    private CheckBox chbxEstudiante;

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
    

    
    Gestion gestion = new Gestion();
    IRepositorioUsuario repositorio = null;
    Usuario nuevoUsuario = null;
    Servicio servicio = null;
    
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
        repositorio = gestion.obtenerRepositorio("SQLite");        
        servicio = new Servicio(repositorio);        
    }  
    
    @FXML
    private void eventClickbtnCrearCuenta(ActionEvent event) {
        
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String correo = txtCorreo.getText();
        String contrasenia = txtContrasenia.getText();
        int celular = Integer.parseInt(txtCelular.getText());
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
        if(chbxDocente.isSelected()){
            rol = EnumRoles.Docente;
        }else{
            rol = EnumRoles.Estudiante;
        }
        
        nuevoUsuario = new Usuario(nombres, apellidos, celular, programa, rol, correo, contrasenia);
        
        if(servicio.validarContrasenaSegura(contrasenia) == "OK"){
            servicio.registrarUsuario(nuevoUsuario);
            JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente", "Cuenta creada", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,servicio.validarContrasenaSegura(contrasenia), "Contraseña Incorrecta",JOptionPane.ERROR_MESSAGE);
        }
        
        
        
        
    }
    
    @FXML
    private void eventClickchbxEstudiante(ActionEvent event) {
        if(chbxEstudiante.isSelected()){
            this.chbxDocente.setSelected(false);
        }
    }
    
    @FXML
    private void eventClickchbxDocente(ActionEvent event) {
        if(chbxDocente.isSelected()){
            this.chbxEstudiante.setSelected(false);
        }
    }
    
}
