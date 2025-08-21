package app.maquina_de_guiones;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import app.maquina_de_guiones.controllers.PanelDeControlController;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("panel_de_control.fxml"));
        Parent root = fxmlLoader.load();

        // Obtener el controlador del panel de control
        PanelDeControlController controller = fxmlLoader.getController();
        // IMPORTANTE: pasarle los HostServices
        controller.setHostServices(getHostServices());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Máquina de Guiones - Panel de Control");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
