package app.maquina_de_guiones;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Dashboard implements Initializable {

    @FXML
    private Button btnStreamers;
    @FXML
    private Button btnColaboradores;
    @FXML
    private Button btnGuiones;
    @FXML
    private Button btnPreguntas;
    @FXML
    private Label lbl_status_bd;
    @FXML
    private AnchorPane rootDashboard;
    @FXML
    private Button btnGuiones_y_preguntas;

    @FXML
    private MenuItem menuSalir;
    @FXML
    private MenuItem menuAcercaDe;
    @FXML
    private MenuItem menuLicencia;
    @FXML
    private MenuItem menuAcercaDe1; // este lo usamos para "Repositorio"

    private HostServices hostServices;
    @FXML
    private Button btnCerrar;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        verificarConexion();
    }

    private void verificarConexion() {
        if (ConexionDB.getConexion() != null && ConexionDB.estaConectado()) {
            lbl_status_bd.setText("✅ Conectado a la base de datos");
            lbl_status_bd.getStyleClass().add("status-ok");
        } else {
            lbl_status_bd.setText("❌ Error de conexión a la base de datos");
            lbl_status_bd.getStyleClass().add("status-error");
        }
    }

    @FXML
    private void abrirStreamers() {
        abrirVentana("/app/maquina_de_guiones/forms/streamers_form.fxml", "Gestión de Streamers");
    }

    @FXML
    private void abrirColaboradores() {
        abrirVentana("/app/maquina_de_guiones/forms/colaboradores_form.fxml", "Gestión de Colaboradores");
    }

    @FXML
    private void abrirGuiones() {
        abrirVentana("/app/maquina_de_guiones/forms/guiones_form.fxml", "Gestión de Guiones");
    }

    @FXML
    private void abrirPreguntas() {
        abrirVentana("/app/maquina_de_guiones/forms/preguntas_form.fxml", "Gestión de Preguntas");
    }

    /**
     * Carga y abre una nueva ventana FXML.
     *
     * @param rutaFXML ruta al archivo FXML
     * @param titulo título de la ventana
     */
    private void abrirVentana(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println("❌ No se pudo cargar la vista: " + rutaFXML);
            ex.printStackTrace();
        }
    }

    // ====== MENÚ SUPERIOR ======
    @FXML
    private void onMenuSalir(ActionEvent event) {
        javafx.application.Platform.exit();
    }

    @FXML
    private void onMenuAcercaDe(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Gestor de Guiones");
        alert.setContentText(
                "Esta aplicación permite gestionar guiones y preguntas, "
                + "mostrando detalles y facilitando la organización.\n\nVersión 1.0"
        );
        alert.showAndWait();
    }

    @FXML
    private void onMenuLicencia(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Licencia");
        alert.setHeaderText("Información de la licencia");
        alert.setContentText(
                "LICENCIA DE USO DEL SOFTWARE\n\n"
                + "© Desarrollado por Soujirito\n\n"
                + "Este software es de código abierto y se distribuye de forma gratuita.\n\n"
                + "Se concede permiso a cualquier persona que obtenga una copia de este software y de los archivos de documentación asociados "
                + "para utilizarlo, copiarlo, modificarlo y distribuirlo con fines educativos, personales o comunitarios.\n\n"
                + "RESTRICCIONES:\n"
                + "- Queda prohibida la venta, alquiler o cualquier forma de comercialización de este software o de sus derivados.\n"
                + "- Cualquier redistribución debe incluir este aviso de licencia y la atribución al autor original.\n\n"
                + "EL SOFTWARE SE ENTREGA 'TAL CUAL', SIN GARANTÍAS DE NINGÚN TIPO, expresas o implícitas, incluyendo pero no limitándose "
                + "a garantías de comerciabilidad, idoneidad para un propósito particular y ausencia de infracciones.\n\n"
                + "Al utilizar este software, aceptas las condiciones anteriores."
        );
        alert.showAndWait();
    }

    @FXML
    private void onMenuRepositorio(ActionEvent event) {
        if (hostServices != null) {
            hostServices.showDocument("https://github.com/Milan3s/maquina-de-hacer-guiones");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo acceder a los servicios del sistema.");
            alert.showAndWait();
        }
    }

    @FXML
    private void accion_btnGuiones_y_preguntas(ActionEvent event) {
        abrirVentana("/app/maquina_de_guiones/front_libros.fxml", "Gestión de Preguntas");
    }

    @FXML
    private void accionCerrar(ActionEvent event) {
        // Obtener el Stage a partir del botón que lanzó el evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
