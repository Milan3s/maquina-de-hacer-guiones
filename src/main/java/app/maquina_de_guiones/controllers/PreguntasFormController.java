package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.DAO.Crud_Guiones;
import app.maquina_de_guiones.DAO.Crud_Preguntas;
import app.maquina_de_guiones.models.Guion;
import app.maquina_de_guiones.models.Pregunta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.GridPane;

public class PreguntasFormController implements Initializable {

    @FXML
    private AnchorPane rootPregunta;
    @FXML
    private VBox vboxPregunta;
    @FXML
    private ComboBox<Guion> cmbGuiones;
    @FXML
    private ComboBox<Pregunta> cmbPreguntas;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextArea txtContenido;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextArea txtObservaciones;
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

    private ObservableList<Guion> listaGuiones;
    private ObservableList<Pregunta> listaPreguntas;
    private Pregunta preguntaActual;
    @FXML
    private GridPane gridPreguntas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarGuiones();

        btnGuardar.setOnAction(this::guardarPregunta);
        btnActualizar.setOnAction(this::actualizarPregunta);
        btnEliminar.setOnAction(this::eliminarPregunta);
        btnNuevo.setOnAction(this::nuevoPregunta);
        btnCancelar.setOnAction(this::cancelar);

        cmbGuiones.setOnAction(e -> {
            Guion guion = cmbGuiones.getSelectionModel().getSelectedItem();
            if (guion != null) {
                cargarPreguntasPorGuion(guion.getId());
                limpiarCamposPregunta();
            }
        });

        cmbPreguntas.setOnAction(e -> {
            Pregunta seleccionada = cmbPreguntas.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                preguntaActual = seleccionada;
                mostrarPregunta(seleccionada);
            }
        });
    }

    private void cargarGuiones() {
        listaGuiones = FXCollections.observableArrayList(Crud_Guiones.obtenerGuiones());
        cmbGuiones.setItems(listaGuiones);
    }

    private void cargarPreguntasPorGuion(int idGuion) {
        List<Pregunta> preguntas = Crud_Preguntas.obtenerPreguntasPorGuion(idGuion);
        listaPreguntas = FXCollections.observableArrayList(preguntas);
        cmbPreguntas.setItems(listaPreguntas);
        cmbPreguntas.getSelectionModel().clearSelection();
    }

    private void mostrarPregunta(Pregunta p) {
        txtTitulo.setText(p.getTitulo());
        txtNombre.setText(p.getNombre());
        txtContenido.setText(p.getContenido());
        txtDescripcion.setText(p.getDescripcion());
        txtObservaciones.setText(p.getObservaciones());

        // Selecciona también el guion si se cambia desde cmbPreguntas
        for (Guion g : listaGuiones) {
            if (g.getId() == p.getIdGuion()) {
                cmbGuiones.getSelectionModel().select(g);
                break;
            }
        }
    }

    @FXML
    private void nuevoPregunta(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    private void guardarPregunta(ActionEvent event) {
        Guion guion = cmbGuiones.getValue();
        if (guion == null || txtTitulo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtContenido.getText().isEmpty()) {
            System.out.println("⚠️ Todos los campos obligatorios deben estar completos.");
            return;
        }

        Pregunta nueva = new Pregunta(
                txtTitulo.getText().trim(),
                txtNombre.getText().trim(),
                txtContenido.getText().trim(),
                txtDescripcion.getText().trim(),
                txtObservaciones.getText().trim(),
                guion.getId()
        );

        if (Crud_Preguntas.insertarPregunta(nueva)) {
            System.out.println("✅ Pregunta guardada correctamente.");
            cargarPreguntasPorGuion(guion.getId());
            limpiarCamposPregunta();
        } else {
            System.out.println("❌ Error al guardar la pregunta.");
        }
    }

    @FXML
    private void actualizarPregunta(ActionEvent event) {
        if (preguntaActual == null) {
            System.out.println("⚠️ No hay pregunta cargada para actualizar.");
            return;
        }

        Guion guion = cmbGuiones.getValue();
        if (guion == null || txtTitulo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtContenido.getText().isEmpty()) {
            System.out.println("⚠️ Todos los campos obligatorios deben estar completos.");
            return;
        }

        preguntaActual.setTitulo(txtTitulo.getText().trim());
        preguntaActual.setNombre(txtNombre.getText().trim());
        preguntaActual.setContenido(txtContenido.getText().trim());
        preguntaActual.setDescripcion(txtDescripcion.getText().trim());
        preguntaActual.setObservaciones(txtObservaciones.getText().trim());
        preguntaActual.setIdGuion(guion.getId());

        if (Crud_Preguntas.actualizarPregunta(preguntaActual)) {
            System.out.println("✅ Pregunta actualizada correctamente.");
            cargarPreguntasPorGuion(guion.getId());
            limpiarFormulario();
        } else {
            System.out.println("❌ Error al actualizar la pregunta.");
        }
    }

    @FXML
    private void eliminarPregunta(ActionEvent event) {
        if (preguntaActual == null) {
            System.out.println("⚠️ No hay pregunta cargada para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar pregunta");
        alerta.setHeaderText("¿Confirmas la eliminación de esta pregunta?");
        alerta.setContentText("Esta acción no se puede deshacer.");

        ButtonType resultado = alerta.showAndWait().orElse(ButtonType.CANCEL);
        if (resultado == ButtonType.OK) {
            if (Crud_Preguntas.eliminarPregunta(preguntaActual.getId())) {
                System.out.println("✅ Pregunta eliminada correctamente.");
                cargarPreguntasPorGuion(preguntaActual.getIdGuion());
                limpiarFormulario();
            } else {
                System.out.println("❌ No se pudo eliminar la pregunta.");
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) rootPregunta.getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        limpiarCamposPregunta();
        cmbGuiones.getSelectionModel().clearSelection();
        cmbPreguntas.getItems().clear();
        preguntaActual = null;
    }

    private void limpiarCamposPregunta() {
        txtTitulo.clear();
        txtNombre.clear();
        txtContenido.clear();
        txtDescripcion.clear();
        txtObservaciones.clear();
    }
}
