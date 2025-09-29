package com.unicauca.proyectogestion.controllers;

import com.unicauca.proyectogestion.access.Factory;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.DevolucionTabla;
import com.unicauca.proyectogestion.service.ServicioFormatoA;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import com.unicauca.proyectogestion.utilities.Navegacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfesorCorreccionesController implements Initializable {

    @FXML
    private TableView<DevolucionTabla> tblFormatos;

    @FXML
    private TableColumn<DevolucionTabla, String> tituloProyecto;

    @FXML
    private TableColumn<DevolucionTabla, String> correoEstudiante;

    @FXML
    private TableColumn<DevolucionTabla, String> correoEstudiante2;

    @FXML
    private TableColumn<DevolucionTabla, String> Coordinador;

    @FXML
    private TableColumn<DevolucionTabla, String> tipoProyecto;

    @FXML
    private TableColumn<DevolucionTabla, Void> resubirFormato;

    @FXML
    private TableColumn<DevolucionTabla, Void> ArchivoAdjunto;

    @FXML
    private ComboBox<String> cbxFiltros;

    @FXML
    private TextField txtBuscar;

    private ServicioFormatoA servicioFormatoA = null;
    private Usuario usuario ;
    private int idProfesor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        IRepositorioFormatoA repositorioFormatoA = Factory.getInstancia().obtenerRepositorioFormatoA("SQLite");
        servicioFormatoA = new ServicioFormatoA(repositorioFormatoA);

        tituloProyecto.setCellValueFactory(new PropertyValueFactory<>("tituloProyecto"));
        tipoProyecto.setCellValueFactory(new PropertyValueFactory<>("tipoProyecto"));
        correoEstudiante.setCellValueFactory(new PropertyValueFactory<>("correoEstudiante1"));
        correoEstudiante2.setCellValueFactory(new PropertyValueFactory<>("correoEstudiante2"));
        Coordinador.setCellValueFactory(new PropertyValueFactory<>("coordinador"));

        // ComboBox con modalidades
        cbxFiltros.setItems(FXCollections.observableArrayList(
                "Investigacion",
                "PracticaProfesional"
        ));
        cbxFiltros.setPromptText("Modalidad");

        ArchivoAdjunto.setCellFactory(param -> new TableCell<>() {
            private final Label link = new Label("Descargar");

            {
                link.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
                link.setOnMouseClicked(event ->    {
                    DevolucionTabla devolucion = getTableView().getItems().get(getIndex());
                    descargarArchivo(devolucion);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(link);
                }
            }
        });

        resubirFormato.setCellFactory(param -> new TableCell<>() {
            private final Label btn = new Label("Resubir");

            {
                btn.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
                btn.setOnMouseClicked(event -> {
                    DevolucionTabla devolucion = getTableView().getItems().get(getIndex());
                    // Supongamos que DevolucionTabla tiene getIdFormato()
                    FormatoATabla formato = servicioFormatoA.obtenerFormato(devolucion.getIdFormato());
                    abrirVentanaResubir(formato);
                });
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

    }

    private void abrirVentanaResubir(FormatoATabla formato) {
        DashboardProfesorController controlador = Navegacion.getController("dashboardProfesor");
        AnchorPane anchorPaneCentral = controlador.getAchrPane();

        ProfesorResubirFormatoController ctrl = Navegacion.cargarEnAnchorPane(anchorPaneCentral, "ProfesorResubirFormato");

        System.out.println("id formato :"+formato.getIdFormato());
        System.out.println("nombre" +formato.getTitulo());

        // Pasar id del formato al nuevo controlador
        ctrl.setDatos(this.usuario, Integer.parseInt(formato.getIdFormato()), formato.getTipoProyecto());

    }

    @FXML
    void desplegarModalidad(ActionEvent event) {
        cbxFiltros.setItems(FXCollections.observableArrayList(
                "Investigacion", "PracticaProfesional"
        ));
    }

    private List<DevolucionTabla> filtrarPorModalidad(List<DevolucionTabla> lista, String modalidad) {
        return lista.stream()
                .filter(f -> f.getTipoProyecto().equalsIgnoreCase(modalidad))
                .collect(Collectors.toList());
    }

    private List<DevolucionTabla> filtrarPorCorreo(List<DevolucionTabla> lista, String correoFiltro) {
        return lista.stream()
                .filter(f ->
                        (f.getCorreoEstudiante1() != null && f.getCorreoEstudiante1().toLowerCase().contains(correoFiltro.toLowerCase())) ||
                                (f.getCorreoEstudiante2() != null && f.getCorreoEstudiante2().toLowerCase().contains(correoFiltro.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    private void cargarFormatos() {
        int idProfesor = this.usuario.getIdUsuario();
        List<DevolucionTabla> formatos = servicioFormatoA.obtenerDevolucionesPorProfesor(idProfesor);
        ObservableList<DevolucionTabla> data = FXCollections.observableArrayList(formatos);
        tblFormatos.setItems(data);
    }

    @FXML
    void btnEventFiltrar(MouseEvent event) {
        String modalidadSeleccionada = cbxFiltros.getSelectionModel().getSelectedItem();

        List<DevolucionTabla> todos = servicioFormatoA.obtenerDevolucionesPorProfesor(idProfesor);

        if (modalidadSeleccionada != null && !modalidadSeleccionada.isEmpty()) {
            List<DevolucionTabla> filtrados = filtrarPorModalidad(todos, modalidadSeleccionada);
            tblFormatos.setItems(FXCollections.observableArrayList(filtrados));
        } else {
            tblFormatos.setItems(FXCollections.observableArrayList(todos));
        }
    }

    @FXML
    void buscarEstudiante(MouseEvent event) {
        String correoBuscado = txtBuscar.getText().trim();
        List<DevolucionTabla> todos = servicioFormatoA.obtenerDevolucionesPorProfesor(idProfesor);
        List<DevolucionTabla> filtrados = filtrarPorCorreo(todos, correoBuscado);
        tblFormatos.setItems(FXCollections.observableArrayList(filtrados));
    }

    private void descargarArchivo(DevolucionTabla devolucion) {
        if (devolucion.getArchivo() != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar devolución");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                fileChooser.setInitialFileName("Devolucion_" + devolucion.getIdDevolucion() + ".pdf");

                File file = fileChooser.showSaveDialog(tblFormatos.getScene().getWindow());

                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(devolucion.getArchivo());
                        fos.flush();
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Archivo descargado en: " + file.getAbsolutePath());
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar el archivo");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No hay archivo disponible para esta devolución");
            alert.showAndWait();
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.idProfesor = usuario.getIdUsuario();
        cargarFormatos();
    }
}
