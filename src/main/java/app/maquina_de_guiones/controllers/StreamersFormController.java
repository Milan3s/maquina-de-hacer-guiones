package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.models.Streamer;
import app.maquina_de_guiones.DAO.Crud_Streamers;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StreamersFormController implements Initializable {

    @FXML
    private ComboBox<Streamer> cmbStreamers;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCanal;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private AnchorPane rootStreamer;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnNuevo;

    private ObservableList<Streamer> listaStreamers;
    private Streamer streamerSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarStreamers();

        cmbStreamers.setOnAction(e -> {
            streamerSeleccionado = cmbStreamers.getSelectionModel().getSelectedItem();
            if (streamerSeleccionado != null) {
                mostrarDatosStreamer(streamerSeleccionado);
            }
        });

        btnGuardar.setOnAction(e -> guardarStreamer());
        btnActualizar.setOnAction(e -> actualizarStreamer());
        btnEliminar.setOnAction(e -> eliminarStreamer());
        btnCancelar.setOnAction(this::cancelar);
        btnNuevo.setOnAction(this::nuevoStreamer);
    }

    private void cargarStreamers() {
        List<Streamer> lista = Crud_Streamers.obtenerStreamers();
        listaStreamers = FXCollections.observableArrayList(lista);
        cmbStreamers.setItems(listaStreamers);
    }

    private void mostrarDatosStreamer(Streamer s) {
        txtNombre.setText(s.getNombre());
        txtCanal.setText(s.getCanal());
        txtDescripcion.setText(s.getDescripcion());
    }

    @FXML
    private void guardarStreamer() {
        Streamer nuevo = new Streamer(
                txtNombre.getText(),
                txtDescripcion.getText(),
                txtCanal.getText()
        );
        if (Crud_Streamers.insertarStreamer(nuevo)) {
            cargarStreamers();
            cmbStreamers.getSelectionModel().selectLast();
            streamerSeleccionado = cmbStreamers.getSelectionModel().getSelectedItem();
            mostrarDatosStreamer(streamerSeleccionado);
        }
    }

    @FXML
    private void actualizarStreamer() {
        if (streamerSeleccionado != null) {
            int idAnterior = streamerSeleccionado.getId();

            streamerSeleccionado.setNombre(txtNombre.getText());
            streamerSeleccionado.setDescripcion(txtDescripcion.getText());
            streamerSeleccionado.setCanal(txtCanal.getText());

            if (Crud_Streamers.actualizarStreamer(streamerSeleccionado)) {
                cargarStreamers();

                for (Streamer s : listaStreamers) {
                    if (s.getId() == idAnterior) {
                        streamerSeleccionado = s;
                        break;
                    }
                }

                cmbStreamers.getSelectionModel().select(streamerSeleccionado);
                mostrarDatosStreamer(streamerSeleccionado);
            }
        }
    }

    @FXML
    private void eliminarStreamer() {
        if (streamerSeleccionado != null) {
            if (Crud_Streamers.eliminarStreamer(streamerSeleccionado.getId())) {
                cargarStreamers();
                limpiarFormulario();
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtCanal.clear();
        txtDescripcion.clear();
        cmbStreamers.getSelectionModel().clearSelection();
        streamerSeleccionado = null;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) rootStreamer.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void nuevoStreamer(ActionEvent event) {
        limpiarFormulario();
    }
}
