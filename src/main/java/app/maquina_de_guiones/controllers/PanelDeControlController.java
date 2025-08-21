package app.maquina_de_guiones.controllers;

import app.maquina_de_guiones.ConexionDB;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.concurrent.Task;
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
import javafx.stage.Stage;

public class PanelDeControlController implements Initializable {

    private HostServices hostServices;

    @FXML
    private Button btnManualUsuario;
    @FXML
    private Button btnInstalarXampp;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnLicencia;
    @FXML
    private Button btnCerrar;
    @FXML
    private Label sms_conexiondb;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comprobarConexionAsync();
        // Siempre activo el botón "Abrir App"
        if (btnIniciar != null) {
            btnIniciar.setDisable(false);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void comprobarConexionAsync() {
        sms_conexiondb.setText("⏳ Probando conexión...");
        sms_conexiondb.getStyleClass().removeAll("db-ok", "db-ko");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                try (Connection conn = ConexionDB.getConexion()) {
                    return conn != null && !conn.isClosed() && conn.isValid(2);
                } catch (SQLException ex) {
                    updateMessage("❌ Debes iniciar XAMPP (MySQL) para que funcione la aplicación");
                    return false;
                }
            }
        };

        task.setOnSucceeded(ev -> {
            boolean ok = Boolean.TRUE.equals(task.getValue());
            sms_conexiondb.getStyleClass().removeAll("db-ok", "db-ko");

            if (ok) {
                sms_conexiondb.setText("✅ Conectado a la base de datos");
                sms_conexiondb.getStyleClass().add("db-ok");

                btnInstalarXampp.setDisable(true);   // XAMPP ya arriba
                btnIniciar.setDisable(false);        // Siempre habilitado
            } else {
                String msg = task.getMessage();
                sms_conexiondb.setText(
                        (msg != null && !msg.isBlank())
                                ? msg
                                : "❌ Debes iniciar XAMPP (MySQL) para que funcione la aplicación"
                );
                sms_conexiondb.getStyleClass().add("db-ko");

                btnInstalarXampp.setDisable(false);  // Permitir abrir XAMPP
                btnIniciar.setDisable(false);        // Siempre habilitado
            }
        });

        task.setOnFailed(ev -> {
            sms_conexiondb.getStyleClass().removeAll("db-ok");
            sms_conexiondb.setText("❌ Debes iniciar XAMPP (MySQL) para que funcione la aplicación");
            sms_conexiondb.getStyleClass().add("db-ko");

            btnInstalarXampp.setDisable(false);
            btnIniciar.setDisable(false); // Siempre habilitado
        });

        Thread th = new Thread(task, "db-check-panel");
        th.setDaemon(true);
        th.start();
    }

    // === BOTONES ===
    @FXML
    private void accionManualUsuario(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Manual de Usuario - Máquina de Guiones");
        alert.setHeaderText("Guía básica para usar la aplicación");
        alert.setContentText(
                "1. Descarga XAMPP desde https://www.apachefriends.org\n\n"
                + "2. Instálalo en C:\\xampp.\n\n"
                + "3. Abre 'xampp-control.exe'.\n\n"
                + "4. Pulsa 'Start' en Apache y MySQL.\n\n"
                + "5. Mantén XAMPP abierto mientras uses Máquina de Guiones."
        );
        alert.showAndWait();
    }

    @FXML
    private void accionInstalarXampp(ActionEvent event) {
        try {
            ProcessBuilder pb = new ProcessBuilder("C:\\xampp\\xampp-control.exe");
            pb.start();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudo abrir XAMPP.\nAsegúrate de que está instalado en C:\\xampp.");
            e.printStackTrace();
        }
    }

    @FXML
    private void accionIniciarApp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/maquina_de_guiones/dashboard.fxml"));
            Parent root = loader.load();

            // Pasar HostServices al Dashboard
            app.maquina_de_guiones.Dashboard dashboardController = loader.getController();
            if (hostServices != null) {
                dashboardController.setHostServices(hostServices);
            }

            Stage stage = new Stage();
            stage.setTitle("Máquina de Guiones - Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar el panel actual
            Stage currentStage = (Stage) btnIniciar.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudo iniciar la aplicación principal.\nRevisa que 'dashboard.fxml' existe en resources.");
            e.printStackTrace();
        }
    }

    @FXML
    private void accionLicencia(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Licencia");
        alert.setHeaderText("Información de la licencia");
        alert.setContentText(
                "LICENCIA DE USO DEL SOFTWARE\n\n"
                + "© Desarrollado por Soujirito\n\n"
                + "Este software es de código abierto y gratuito.\n\n"
                + "Se permite usar, copiar, modificar y distribuir para fines educativos o personales.\n\n"
                + "RESTRICCIONES:\n"
                + "- Prohibida la venta o alquiler.\n"
                + "- Redistribución debe mantener este aviso.\n\n"
                + "EL SOFTWARE SE ENTREGA 'TAL CUAL', SIN GARANTÍAS."
        );
        alert.showAndWait();
    }

    @FXML
    private void accionCerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
