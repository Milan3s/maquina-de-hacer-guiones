package app.maquina_de_guiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión con la base de datos MySQL.
 * Utiliza una nueva conexión para cada solicitud (seguro para múltiples operaciones).
 * 
 * Asegúrate de tener el conector MySQL en tu classpath.
 * 
 * @author Milanes
 */
public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/maquina_de_hacer_guiones";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Crea y devuelve una nueva conexión a la base de datos.
     * @return nueva conexión activa, o null si falla
     */
    public static Connection getConexion() {
        try {
            Connection nuevaConexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("✅ Conexión establecida con la base de datos.");
            return nuevaConexion;
        } catch (SQLException ex) {
            System.err.println("❌ Error al conectar con la base de datos: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Verifica si se puede abrir una conexión (usado para testear el estado).
     * @return true si se puede abrir una conexión, false si no
     */
    public static boolean estaConectado() {
        try (Connection conn = getConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Método sin efecto práctico en esta versión (usado antes para cerrar conexión global).
     * Aquí no se mantiene conexión persistente, por lo que no se cierra nada.
     */
    public static void cerrarConexion() {
        // Ya no es necesario mantener o cerrar una conexión persistente.
        System.out.println("ℹ️ No hay conexión persistente que cerrar.");
    }
}
