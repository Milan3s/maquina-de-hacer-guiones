package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.models.Guion;
import app.maquina_de_guiones.models.Streamer;
import app.maquina_de_guiones.models.Colaborador;
import app.maquina_de_guiones.DAO.Crud_Guiones;
import app.maquina_de_guiones.DAO.Crud_Streamers;
import app.maquina_de_guiones.DAO.Crud_Colaboradores;

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

public class GuionesFormController implements Initializable {

    @FXML private AnchorPane rootGuion;
    @FXML private ComboBox<Streamer> cmbStreamers;
    @FXML private ComboBox<Colaborador> cmbColaboradores;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextArea txtContenido;
    @FXML private Button btnNuevo;
    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;

    private ObservableList<Streamer> listaStreamers;
    private ObservableList<Colaborador> listaColaboradores;
    private Guion guionActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarStreamers();
        cargarColaboradores();

        btnGuardar.setOnAction(this::guardarGuion);
        btnActualizar.setOnAction(this::actualizarGuion);
        btnEliminar.setOnAction(this::eliminarGuion);
        btnNuevo.setOnAction(this::nuevoGuion);
        btnCancelar.setOnAction(this::cancelar);

        cmbColaboradores.setOnAction(e -> {
            Colaborador seleccionado = cmbColaboradores.getValue();
            if (seleccionado != null) {
                guionActual = Crud_Guiones.obtenerGuionPorColaborador(seleccionado.getId());
                if (guionActual != null) {
                    mostrarGuion(guionActual);
                } else {
                    limpiarCamposGuion();
                    cmbStreamers.getSelectionModel().clearSelection();
                }
            }
        });

        cmbStreamers.setOnAction(e -> {
            Streamer seleccionado = cmbStreamers.getValue();
            if (seleccionado != null) {
                guionActual = Crud_Guiones.obtenerGuionPorStreamer(seleccionado.getId());
                if (guionActual != null) {
                    mostrarGuion(guionActual);
                } else {
                    limpiarCamposGuion();
                    cmbColaboradores.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void cargarStreamers() {
        listaStreamers = FXCollections.observableArrayList(Crud_Streamers.obtenerStreamers());
        cmbStreamers.setItems(listaStreamers);
    }

    private void cargarColaboradores() {
        listaColaboradores = FXCollections.observableArrayList(Crud_Colaboradores.obtenerColaboradores());
        cmbColaboradores.setItems(listaColaboradores);
    }

    private void mostrarGuion(Guion g) {
        txtTitulo.setText(g.getTitulo());
        txtNombre.setText(g.getNombre());
        txtDescripcion.setText(g.getDescripcion());
        txtContenido.setText(g.getContenido());

        // Streamer
        for (Streamer s : listaStreamers) {
            if (s.getId() == g.getIdStreamer()) {
                cmbStreamers.getSelectionModel().select(s);
                break;
            }
        }

        // Colaborador
        if (g.getIdColaborador() != null) {
            for (Colaborador c : listaColaboradores) {
                if (c.getId() == g.getIdColaborador()) {
                    cmbColaboradores.getSelectionModel().select(c);
                    break;
                }
            }
        } else {
            cmbColaboradores.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void guardarGuion(ActionEvent event) {
        String titulo = txtTitulo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String contenido = txtContenido.getText().trim();
        Streamer streamer = cmbStreamers.getValue();
        Colaborador colaborador = cmbColaboradores.getValue();

        if (streamer == null || titulo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || contenido.isEmpty()) {
            System.out.println("⚠️ Todos los campos obligatorios deben estar completos.");
            return;
        }

        Guion nuevo = new Guion(titulo, nombre, contenido, descripcion, streamer.getId(),
                (colaborador != null) ? colaborador.getId() : null);

        if (Crud_Guiones.insertarGuion(nuevo)) {
            System.out.println("✅ Guion creado correctamente.");
            limpiarFormulario();
        } else {
            System.out.println("❌ Error al guardar el guion.");
        }
    }

    @FXML
    private void actualizarGuion(ActionEvent event) {
        if (guionActual == null) {
            System.out.println("⚠️ No hay guion cargado para actualizar.");
            return;
        }

        String titulo = txtTitulo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String contenido = txtContenido.getText().trim();
        Streamer streamer = cmbStreamers.getValue();
        Colaborador colaborador = cmbColaboradores.getValue();

        if (streamer == null || titulo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || contenido.isEmpty()) {
            System.out.println("⚠️ Todos los campos obligatorios deben estar completos.");
            return;
        }

        guionActual.setTitulo(titulo);
        guionActual.setNombre(nombre);
        guionActual.setDescripcion(descripcion);
        guionActual.setContenido(contenido);
        guionActual.setIdStreamer(streamer.getId());
        guionActual.setIdColaborador((colaborador != null) ? colaborador.getId() : null);

        if (Crud_Guiones.actualizarGuion(guionActual)) {
            System.out.println("✅ Guion actualizado correctamente.");
            limpiarFormulario();
        } else {
            System.out.println("❌ Error al actualizar el guion.");
        }
    }

    @FXML
    private void eliminarGuion(ActionEvent event) {
        if (guionActual == null) {
            System.out.println("⚠️ No hay guion cargado para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar este guion?");
        alert.setContentText("Esta acción no se puede deshacer.");

        ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (resultado == ButtonType.OK) {
            boolean eliminado = Crud_Guiones.eliminarGuion(guionActual.getId());
            if (eliminado) {
                System.out.println("✅ Guion eliminado correctamente.");
                limpiarFormulario();
            } else {
                System.out.println("❌ No se pudo eliminar el guion.");
            }
        } else {
            System.out.println("❎ Eliminación cancelada por el usuario.");
        }
    }

    @FXML
    private void nuevoGuion(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) rootGuion.getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        limpiarCamposGuion();
        cmbStreamers.getSelectionModel().clearSelection();
        cmbColaboradores.getSelectionModel().clearSelection();
        guionActual = null;
    }

    private void limpiarCamposGuion() {
        txtTitulo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtContenido.clear();
    }
}
