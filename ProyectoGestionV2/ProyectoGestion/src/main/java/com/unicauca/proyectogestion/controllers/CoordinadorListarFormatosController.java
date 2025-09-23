package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.service.Servicio;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;

import java.util.List;

public class CoordinadorListarFormatosController {

    @FXML
    private ImageView btnBuscar;

    @FXML
    private ImageView btnfiltrar;

    @FXML
    private ComboBox<String> cbxFiltros;

    @FXML
    private TableView<FormatoATabla> tblFormatos;

    @FXML
    private TableColumn<FormatoATabla, String> colCorreo;

    @FXML
    private TableColumn<FormatoATabla, String> colDirector;

    @FXML
    private TableColumn<FormatoATabla, String> colTipoProyecto;

    @FXML
    private TableColumn<FormatoATabla, String> colEvaluado;

    @FXML
    private TableColumn<FormatoATabla, Void> colEvaluar;

    @FXML
    private TextField txtBuscar;
    private Servicio servicio = null;

    @FXML
    public void initialize() {

        IRepositorioUsuario repositorio = Gestion.getInstancia().obtenerRepositorio("SQLite");
        servicio = new Servicio(repositorio);

        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoEstudiante"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colTipoProyecto.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));
        colEvaluado.setCellValueFactory(new PropertyValueFactory<>("evaluado"));

        // Agregar botón Evaluar
        colEvaluar.setCellFactory(param -> new TableCell<>() {
            private final Label btn = new Label("Evaluar");

            {
                btn.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
                btn.setOnMouseClicked(event -> {
                    FormatoATabla formato = getTableView().getItems().get(getIndex());
                    abrirVentanaEvaluar(formato);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        cargarFormatos();
    }
/*
    private List<FormatoATabla> filtrarPorCorreo(List<FormatoATabla> lista, String correoFiltro) {
        return lista.stream()
                .filter(f -> f.getCorreoEstudiante().toLowerCase().contains(correoFiltro.toLowerCase()))
                .toList();
    }

 */
    private void cargarFormatos() {
        List<FormatoATabla> formatos = servicio.obtenerFormatos();
        ObservableList<FormatoATabla> data = FXCollections.observableArrayList(formatos);
        tblFormatos.setItems(data);
    }

    private void abrirVentanaEvaluar(FormatoATabla formato) {
        System.out.println("Evaluando proyecto de: " + formato.getCorreoEstudiante());
        Navegacion.cambiarVista("CoordinadorEvaluarFormatoController");
    }

    @FXML
    void btnEventFiltrar(MouseEvent event) {
        // lógica de filtros
    }

    @FXML
    void buscarEstudiante(MouseEvent event) {
        /*
        String correoBuscado = txtBuscar.getText().trim();
        List<FormatoATabla> todos = servicio.obtenerFormatos();
        List<FormatoATabla> filtrados = filtrarPorCorreo(todos, correoBuscado);
        tblFormatos.setItems(FXCollections.observableArrayList(filtrados));

         */
    }
}
