package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.models.Colaborador;
import app.maquina_de_guiones.DAO.Crud_Colaboradores;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ColaboradoresFormController implements Initializable {

    @FXML
    private AnchorPane rootColaborador;
    @FXML
    private ComboBox<Colaborador> cmbColaboradores;
    @FXML
    private GridPane gridColaborador;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtCanal;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;

    private ObservableList<Colaborador> listaColaboradores;
    private Colaborador colaboradorSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarColaboradores();

        cmbColaboradores.setOnAction(e -> {
            colaboradorSeleccionado = cmbColaboradores.getSelectionModel().getSelectedItem();
            if (colaboradorSeleccionado != null) {
                mostrarDatosColaborador(colaboradorSeleccionado);
            }
        });

        btnGuardar.setOnAction(this::guardarColaborador);
        btnActualizar.setOnAction(this::actualizarColaborador);
        btnEliminar.setOnAction(this::eliminarColaborador);
        btnCancelar.setOnAction(this::cancelar);
        btnNuevo.setOnAction(this::nuevoColaborador);
    }

    private void cargarColaboradores() {
        List<Colaborador> lista = Crud_Colaboradores.obtenerColaboradores();
        listaColaboradores = FXCollections.observableArrayList(lista);
        cmbColaboradores.setItems(listaColaboradores);
    }

    private void mostrarDatosColaborador(Colaborador c) {
        txtNombre.setText(c.getNombre());
        txtDescripcion.setText(c.getDescripcion());
        txtCanal.setText(c.getCanal());
    }

    @FXML
    private void guardarColaborador(ActionEvent event) {
        Colaborador nuevo = new Colaborador(
                txtNombre.getText(),
                txtDescripcion.getText(),
                txtCanal.getText()
        );

        if (Crud_Colaboradores.insertarColaborador(nuevo)) {
            cargarColaboradores();
            cmbColaboradores.getSelectionModel().selectLast();
            colaboradorSeleccionado = cmbColaboradores.getSelectionModel().getSelectedItem();
            mostrarDatosColaborador(colaboradorSeleccionado);
        }
    }

    @FXML
    private void actualizarColaborador(ActionEvent event) {
        if (colaboradorSeleccionado != null) {
            int idAnterior = colaboradorSeleccionado.getId();

            colaboradorSeleccionado.setNombre(txtNombre.getText());
            colaboradorSeleccionado.setDescripcion(txtDescripcion.getText());
            colaboradorSeleccionado.setCanal(txtCanal.getText());

            if (Crud_Colaboradores.actualizarColaborador(colaboradorSeleccionado)) {
                cargarColaboradores();

                for (Colaborador c : listaColaboradores) {
                    if (c.getId() == idAnterior) {
                        colaboradorSeleccionado = c;
                        break;
                    }
                }

                cmbColaboradores.getSelectionModel().select(colaboradorSeleccionado);
                mostrarDatosColaborador(colaboradorSeleccionado);
            }
        }
    }

    @FXML
    private void eliminarColaborador(ActionEvent event) {
        if (colaboradorSeleccionado != null) {
            if (Crud_Colaboradores.eliminarColaborador(colaboradorSeleccionado.getId())) {
                cargarColaboradores();
                limpiarFormulario();
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtCanal.clear();
        cmbColaboradores.getSelectionModel().clearSelection();
        colaboradorSeleccionado = null;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) rootColaborador.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void nuevoColaborador(ActionEvent event) {
        limpiarFormulario();
    }
}
