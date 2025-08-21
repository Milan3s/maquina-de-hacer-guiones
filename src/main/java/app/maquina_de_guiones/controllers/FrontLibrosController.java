package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.DAO.Crud_Guiones;
import app.maquina_de_guiones.DAO.Crud_Libros;
import app.maquina_de_guiones.DAO.Crud_Preguntas;
import app.maquina_de_guiones.models.Guion;
import app.maquina_de_guiones.models.Libro;
import app.maquina_de_guiones.models.Pregunta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FrontLibrosController {

    @FXML
    private ComboBox<Libro> cbLibros;
    @FXML
    private ComboBox<Guion> cbGuiones;      // <-- ahora tipado
    @FXML
    private TextArea taContenidoLibro;
    @FXML
    private TextArea taContenidoPreguntas;
    @FXML
    private Label lblTitulo;

    private final ObservableList<Libro> libros = FXCollections.observableArrayList();
    private final ObservableList<Guion> guiones = FXCollections.observableArrayList();

    private void initialize() {
        taContenidoLibro.setWrapText(true);
        taContenidoPreguntas.setWrapText(true);

        // Libros: renderizar por título
        cbLibros.setConverter(new StringConverter<>() {
            @Override
            public String toString(Libro l) {
                return l == null ? "" : safe(l.getTitulo());
            }

            @Override
            public Libro fromString(String s) {
                return null;
            }
        });
        cbLibros.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Libro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : safe(item.getTitulo()));
            }
        });
        cbLibros.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Libro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : safe(item.getTitulo()));
            }
        });

        // Guiones: renderizar por título
        cbGuiones.setConverter(new StringConverter<>() {
            @Override
            public String toString(Guion g) {
                return g == null ? "" : safe(g.getTitulo());
            }

            @Override
            public Guion fromString(String s) {
                return null;
            }
        });
        cbGuiones.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Guion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : safe(item.getTitulo()));
            }
        });

        cargarLibrosDesdeBD();
        cargarGuionesDesdeBD();

        cbLibros.setItems(libros);
        cbGuiones.setItems(guiones);

        // Seleccionar primer libro si hay
        if (!libros.isEmpty()) {
            cbLibros.getSelectionModel().selectFirst();
            sincronizarGuionConLibro(cbLibros.getSelectionModel().getSelectedItem());
        } else {
            limpiarDetalle();
        }

        // Reaccionar a cambios de selección
        cbLibros.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> sincronizarGuionConLibro(selected));
        cbGuiones.getSelectionModel().selectedItemProperty().addListener((obs, old, g) -> mostrarPorGuionSeleccionado(g));
    }

    // --- Eventos FXML ---
    @FXML
    private void onLibroSeleccionado() {
        sincronizarGuionConLibro(cbLibros.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onRecargarLibros() {
        Integer idPrevio = Optional.ofNullable(cbLibros.getSelectionModel().getSelectedItem()).map(Libro::getId).orElse(null);

        cargarLibrosDesdeBD();
        cbLibros.setItems(libros);

        if (idPrevio != null) {
            for (Libro l : libros) {
                if (Objects.equals(l.getId(), idPrevio)) {
                    cbLibros.getSelectionModel().select(l);
                    sincronizarGuionConLibro(l);
                    return;
                }
            }
        }
        if (!libros.isEmpty()) {
            cbLibros.getSelectionModel().selectFirst();
            sincronizarGuionConLibro(cbLibros.getSelectionModel().getSelectedItem());
        } else {
            cbLibros.getSelectionModel().clearSelection();
            limpiarDetalle();
        }
    }

    @FXML
    private void onCerrar(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    // --- Lógica ---
    /**
     * Al elegir libro: selecciona su guion en el combo y pinta la vista previa.
     */
    private void sincronizarGuionConLibro(Libro libro) {
        if (libro == null) {
            cbGuiones.getSelectionModel().clearSelection();
            limpiarDetalle();
            return;
        }
        if (lblTitulo != null) {
            lblTitulo.setText(safe(libro.getTitulo()));
        }

        // Seleccionar en cbGuiones el guion con id == libro.idGuion
        Guion match = null;
        for (Guion g : guiones) {
            if (g.getId() == libro.getIdGuion()) {
                match = g;
                break;
            }
        }
        if (match != null) {
            cbGuiones.getSelectionModel().select(match);
            mostrarPorGuionSeleccionado(match); // pinta contenido/preguntas del guion
        } else {
            cbGuiones.getSelectionModel().clearSelection();
            // Fallback: usar datos que venían en el Libro (JOIN)
            mostrarLibroSeleccionado(libro);
        }
    }

    /**
     * Pinta usando el guion seleccionado: contenido del guion + preguntas del
     * guion.
     */
    private void mostrarPorGuionSeleccionado(Guion guion) {
        if (guion == null) {
            limpiarDetalle();
            return;
        }

        StringBuilder contenido = new StringBuilder();
        if (!isBlank(guion.getTitulo())) {
            contenido.append("== ").append(guion.getTitulo()).append(" ==\n\n");
        }
        if (!isBlank(guion.getContenido())) {
            contenido.append(guion.getContenido());
        } else {
            contenido.append("(Sin contenido de guion)");
        }
        taContenidoLibro.setText(contenido.toString());

        // Cargar preguntas del guion elegido
        List<Pregunta> preguntas = Crud_Preguntas.obtenerPreguntasPorGuion(guion.getId());
        taContenidoPreguntas.setText(formatearPreguntas(preguntas));
    }

    /**
     * Fallback si no hay guion cargado en combos: usa lo que trae Libro por el
     * JOIN.
     */
    private void mostrarLibroSeleccionado(Libro libro) {
        if (libro == null) {
            limpiarDetalle();
            return;
        }

        StringBuilder contenido = new StringBuilder();
        if (!isBlank(libro.getGuionTitulo())) {
            contenido.append("== ").append(libro.getGuionTitulo()).append(" ==\n\n");
        }
        if (!isBlank(libro.getGuionContenido())) {
            contenido.append(libro.getGuionContenido());
        } else {
            contenido.append("(Sin contenido de guion)");
        }
        if (!isBlank(libro.getDescripcion())) {
            contenido.append("\n\nDescripción del libro: ").append(libro.getDescripcion());
        }
        taContenidoLibro.setText(contenido.toString().trim());

        taContenidoPreguntas.setText(formatearPreguntas(libro.getPreguntas()));
    }

    // --- Datos ---
    private void cargarLibrosDesdeBD() {
        libros.clear();
        List<Libro> desdeDAO = Crud_Libros.listarLibrosConGuionYPreguntas();
        if (desdeDAO != null) {
            libros.addAll(desdeDAO);
        }
    }

    private void cargarGuionesDesdeBD() {
        guiones.clear();
        List<Guion> items = Crud_Guiones.obtenerGuiones();
        if (items != null) {
            guiones.addAll(items);
        }
    }

    // --- Utilidades ---
    private String formatearPreguntas(List<Pregunta> preguntas) {
        if (preguntas == null || preguntas.isEmpty()) {
            return "(Sin preguntas para este guion)";
        }
        StringBuilder sb = new StringBuilder();
        int num = 1;
        for (Pregunta p : preguntas) {
            String tituloONombre = !isBlank(p.getTitulo()) ? p.getTitulo() : p.getNombre();
            sb.append(num++).append(". ");
            if (!isBlank(tituloONombre)) {
                sb.append(tituloONombre).append("\n");
            }
            if (!isBlank(p.getContenido())) {
                sb.append(p.getContenido()).append("\n");
            }
            if (!isBlank(p.getDescripcion())) {
                sb.append("  (").append(p.getDescripcion()).append(")\n");
            }
            if (!isBlank(p.getObservaciones())) {
                sb.append("  Nota: ").append(p.getObservaciones()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    private void limpiarDetalle() {
        if (lblTitulo != null) {
            lblTitulo.setText("");
        }
        taContenidoLibro.clear();
        taContenidoPreguntas.clear();
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
