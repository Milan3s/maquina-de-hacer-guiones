module app.maquina_de_guiones {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.base;

    // Permitir acceso de JavaFX a controladores y clases principales
    opens app.maquina_de_guiones to javafx.fxml;
    opens app.maquina_de_guiones.controllers to javafx.fxml;

    exports app.maquina_de_guiones;
    exports app.maquina_de_guiones.controllers;
}
