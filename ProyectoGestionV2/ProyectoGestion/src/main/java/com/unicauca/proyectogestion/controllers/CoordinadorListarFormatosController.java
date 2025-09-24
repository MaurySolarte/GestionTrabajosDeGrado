package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.service.ServicioUsuario;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.stream.Collectors;

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
    private TableColumn<FormatoATabla, String> correoEstudiante;

    @FXML
    private TableColumn<FormatoATabla, String> director;

    @FXML
    private TableColumn<FormatoATabla, String> tipoDeProyecto;

    @FXML
    private TableColumn<FormatoATabla, String> evaluado;

    @FXML
    private TableColumn<FormatoATabla, Void> evaluar;

    @FXML
    private TextField txtBuscar;
    private ServicioUsuario servicioUsuario = null;
    private ServicioFormatoA servicioFormatoA = null;

    @FXML
    public void initialize() {

        IRepositorioUsuario repositorioUsuario = Factory.getInstancia().obtenerRepositorioUsuario("SQLite");
        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioUsuario = new ServicioUsuario(repositorioUsuario);
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);

        correoEstudiante.setCellValueFactory(new PropertyValueFactory<>("correoEstudiante"));
        director.setCellValueFactory(new PropertyValueFactory<>("director"));
        tipoDeProyecto.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));
        evaluado.setCellValueFactory(new PropertyValueFactory<>("evaluado"));

        // Agregar botón Evaluar
        evaluar.setCellFactory(param -> new TableCell<>() {
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

    private List<FormatoATabla> filtrarPorCorreo(List<FormatoATabla> lista, String correoFiltro) {
        return lista.stream()
                .filter(f -> f.getCorreoEstudiante().toLowerCase().contains(correoFiltro.toLowerCase())).collect(Collectors.toList());
    }


    private void cargarFormatos() {
        List<FormatoATabla> formatos = servicioFormatoA.obtenerFormatos();
        ObservableList<FormatoATabla> data = FXCollections.observableArrayList(formatos);
        tblFormatos.setItems(data);
    }

    private void abrirVentanaEvaluar(FormatoATabla formato) {
        System.out.println("Evaluando proyecto de: " + formato.getCorreoEstudiante());
        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();
        Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorEvaluarFormato");

    }

    @FXML
    void btnEventFiltrar(MouseEvent event) {
        // lógica de filtros
    }

    @FXML
    void buscarEstudiante(MouseEvent event) {

        String correoBuscado = txtBuscar.getText().trim();
        List<FormatoATabla> todos = servicioFormatoA.obtenerFormatos();
        List<FormatoATabla> filtrados = filtrarPorCorreo(todos, correoBuscado);
        tblFormatos.setItems(FXCollections.observableArrayList(filtrados));


    }
}
