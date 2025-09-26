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

import javafx.event.ActionEvent;
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
    private TableColumn<FormatoATabla, String> tituloProyecto;

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
        tituloProyecto.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        // 🔹 Aquí cargas las modalidades en el ComboBox
        cbxFiltros.setItems(FXCollections.observableArrayList(
                "Investigacion",
                "PracticaProfesional"
        ));
        cbxFiltros.setPromptText("Modalidad");
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
    @FXML
    void eventBtnVolver(MouseEvent event) {
        Navegacion.cambiarVista("dashboardCoordinador");
    }

    @FXML
    void eventDesplegar(ActionEvent event) {
        cbxFiltros.setItems(FXCollections.observableArrayList(
                "Investigacion", "PracticaProfesional"
        ));
    }

    private List<FormatoATabla> filtrarPorModalidad(List<FormatoATabla> lista, String modalidad) {
        return lista.stream()
                .filter(f -> f.getTipoProyecto().equalsIgnoreCase(modalidad))
                .collect(Collectors.toList());
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
        DashboardCoordinadorController controlador = Navegacion.getController("dashboardCoordinador");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();

        // Cargar FXML y obtener controlador
        CoordinadorEvaluarFormatoController ctrl = (CoordinadorEvaluarFormatoController)
                Navegacion.cargarEnAnchorPane(anchorPaneCentral, "CoordinadorEvaluarFormato");
        System.out.println("id formato :"+formato.getIdFormato());
        System.out.println("nombre" +formato.getTitulo());

        // Pasar id del formato al nuevo controlador
        ctrl.setIdFormato(formato.getIdFormato(), formato.getTipoProyecto());
    }

    @FXML
    void btnEventFiltrar(MouseEvent event) {
        String modalidadSeleccionada = cbxFiltros.getSelectionModel().getSelectedItem();
        List<FormatoATabla> todos = servicioFormatoA.obtenerFormatos();

        if (modalidadSeleccionada != null && !modalidadSeleccionada.isEmpty()) {
            List<FormatoATabla> filtrados = filtrarPorModalidad(todos, modalidadSeleccionada);
            tblFormatos.setItems(FXCollections.observableArrayList(filtrados));
        } else {
            tblFormatos.setItems(FXCollections.observableArrayList(todos)); // muestra todos si no hay filtro
        }
    }

    @FXML
    void buscarEstudiante(MouseEvent event) {

        String correoBuscado = txtBuscar.getText().trim();
        List<FormatoATabla> todos = servicioFormatoA.obtenerFormatos();
        List<FormatoATabla> filtrados = filtrarPorCorreo(todos, correoBuscado);
        tblFormatos.setItems(FXCollections.observableArrayList(filtrados));


    }
}
