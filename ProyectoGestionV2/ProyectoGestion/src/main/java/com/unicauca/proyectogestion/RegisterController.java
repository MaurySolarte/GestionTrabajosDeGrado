
package com.unicauca.proyectogestion;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.*;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    

    //Variables globales de objetos que se usan en toda la clase.
    //Gestion gestion = new Gestion();
    //IRepositorioUsuario repositorio = null;
    private Usuario nuevoUsuario = null;
    
    private Servicio servicio = null;
    
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
        
        IRepositorioUsuario repositorio = Gestion.getInstancia().obtenerRepositorio("SQLite");
        servicio = new Servicio(repositorio);
        
    }  

 
    
    
    @FXML
    private void eventClickbtnCrearCuenta(ActionEvent event) {
                        
        if(validarCamposVacios() == false){
            capturarDatosUsuario();
            if(validarContrasenia()){
            registrarUsuario();
            }
        }        
                
    }
    
    private boolean validarCamposVacios(){        
        
        if(txtNombres.getText() == ""){                        
            mostrarAlerta("Campos vacíos", "Por favor ingrese sus nombres.", Alert.AlertType.WARNING);
            return true;
        }else if(txtApellidos.getText() == ""){            
            mostrarAlerta("Campos vacíos", "Por favor ingrese sus apellidos.", Alert.AlertType.WARNING);
            return true;
        }else if(txtCorreo.getText() == ""){            
            mostrarAlerta("Campos vacíos", "Por favor ingrese un correo.", Alert.AlertType.WARNING);
            return true;
        }else if(txtContrasenia.getText() == ""){            
            mostrarAlerta("Campos vacíos", "Por favor ingrese una contraseña.", Alert.AlertType.WARNING);
            return true;
        }else if(cbxPrograma.getValue() == null){            
            mostrarAlerta("Campos vacíos", "Por favor seleccione el programa al que pertenece.", Alert.AlertType.WARNING);
            return true;
        }else if(chbxDocente.isSelected() == false && chbxEstudiante.isSelected() == false){            
            mostrarAlerta("Por favor seleccione un rol.", "Campos vacíos", Alert.AlertType.WARNING);
            return true;
        }      
        return false;
        
    }
    
    private void capturarDatosUsuario(){
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
            rol = EnumRoles.Profesor;
        }else{
            rol = EnumRoles.Estudiante;
        }
        
        //nuevoUsuario = new Usuario(nombres, apellidos, celular, programa, rol, correo, contrasenia);
        
    }
    
    private void registrarUsuario(){
        try{        
            if(servicio.registrarUsuario(nuevoUsuario)){                                
                mostrarAlerta("Cuenta creada", "Cuenta creada exitosamente", Alert.AlertType.INFORMATION);
            }
            else{                
                mostrarAlerta("Cuenta existente.", "Ya existe una cuenta registrada con ese correo.", Alert.AlertType.ERROR);
            }
        }catch(SQLException ex){            
            mostrarAlerta("Error al crear cuenta.", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }    
    
    private boolean validarContrasenia(){
        if(servicio.validarContrasenaSegura(nuevoUsuario.getContrasenia()) == "OK"){                            
            return true;
        }
        else{            
            mostrarAlerta("Contraseña Incorrecta", servicio.validarContrasenaSegura(nuevoUsuario.getContrasenia()), Alert.AlertType.ERROR);
            return false;
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);

        // Cambiar título e ícono de ventana
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        // Crear un Label personalizado para el mensaje
            Label etiqueta = new Label(mensaje);
        etiqueta.setWrapText(true);
        etiqueta.setStyle("-fx-font-Tebuchet MS: 14px; -fx-font-family: 'Segoe UI'; -fx-text-fill: #2c3e50;");

        // Meter el Label en un contenedor para darle padding
            VBox contenedor = new VBox(etiqueta);
        contenedor.setSpacing(10);
        contenedor.setPadding(new Insets(10));

        alerta.getDialogPane().setContent(contenedor);

        // Aplicar estilo al cuadro de diálogo completo
        alerta.getDialogPane().setStyle(
            "-fx-background-color: #f9f9f9; " +
            "-fx-border-color: #ABBEF6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );

        // Cambiar estilo de los botones
        alerta.getDialogPane().lookupButton(ButtonType.OK)
              .setStyle("-fx-background-color: #1E2C9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 7px;");

        alerta.showAndWait();
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
     
     @FXML
    void eventClicklblVolver(MouseEvent event) {
         Navegacion.cambiarVista("login");
    }
    
}
