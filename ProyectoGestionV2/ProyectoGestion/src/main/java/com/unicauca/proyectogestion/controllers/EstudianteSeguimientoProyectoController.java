package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.domain.Estudiante;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.Format;
import java.util.ResourceBundle;

public class EstudianteSeguimientoProyectoController implements Initializable {

    @FXML
    private Label lblEstadoActual;

    @FXML
    private Label lblFechaInicio;

    @FXML
    private Label lblNombreEstudiante;

    @FXML
    private Label lblTipoProyecto;

    @FXML
    private Label lblTutor;

    @FXML
    private Label lblTitulo;

    @FXML
    private Tab tbpnHistorial;

    @FXML
    private Tab tbpnInfoProyecto;


    private Usuario usuario;
    private FormatoATabla formatoA;
    private ServicioFormatoA servicioFormatoA;
    private ServicioUsuario servicioUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);
//        var repositorioUsuario = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
//        servicioUsuario = new ServicioUsuario(repositorioUsuario);
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void inicializarUsuario(Usuario usuario) {
        setUsuario(usuario);
        //cargarUsuario();
       cargarProyecto();
    }

    private void cargarProyecto() {
        if (usuario == null) return;
        formatoA = servicioFormatoA.obtenerFormatoPorCorreo(usuario.getEmail());
        System.out.println(usuario.getEmail());
        //System.out.println(formatoA.getCorreoEstudiante());
        System.out.println("Ingresa al metodo");
        if (formatoA != null) {

            lblTitulo.setText(formatoA.getTitulo());
            lblTipoProyecto.setText(formatoA.getTipoProyecto());
            lblTutor.setText(formatoA.getDirector());
            lblNombreEstudiante.setText(usuario.getNombres());
            lblFechaInicio.setText(formatoA.getFechaInicio());
            lblEstadoActual.setText(formatoA.getEstadoActual());

        } else {
            System.out.println("FORMATO NO ENCONTRADO EN LA BD");
            lblTitulo.setText("-");
            lblTipoProyecto.setText("No registrado");
            lblTutor.setText("-");
            lblFechaInicio.setText("-");
            lblEstadoActual.setText("-");
        }
        }
    }



