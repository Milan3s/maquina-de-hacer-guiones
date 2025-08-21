package app.maquina_de_guiones.DAO;

import app.maquina_de_guiones.models.Streamer;
import app.maquina_de_guiones.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Crud_Streamers {

    /**
     * Obtener todos los streamers para mostrar en ComboBox.
     */
    public static ArrayList<Streamer> obtenerStreamers() {
        ArrayList<Streamer> lista = new ArrayList<>();
        String sql = "SELECT * FROM streamers";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return lista;

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Streamer s = new Streamer(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("canal")
                    );
                    lista.add(s);
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener streamers: " + ex.getMessage());
        }

        return lista;
    }

    /**
     * Insertar un nuevo streamer en la base de datos.
     */
    public static boolean insertarStreamer(Streamer s) {
        String sql = "INSERT INTO streamers (nombre, descripcion, canal) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, s.getNombre());
                ps.setString(2, s.getDescripcion());
                ps.setString(3, s.getCanal());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Streamer insertado correctamente." : "⚠️ No se insertó el streamer.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar streamer: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Actualizar un streamer existente por ID.
     */
    public static boolean actualizarStreamer(Streamer s) {
        String sql = "UPDATE streamers SET nombre = ?, descripcion = ?, canal = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, s.getNombre());
                ps.setString(2, s.getDescripcion());
                ps.setString(3, s.getCanal());
                ps.setInt(4, s.getId());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Streamer actualizado correctamente." : "⚠️ No se actualizó el streamer.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar streamer: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un streamer por ID.
     */
    public static boolean eliminarStreamer(int id) {
        String sql = "DELETE FROM streamers WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Streamer eliminado correctamente." : "⚠️ No se eliminó el streamer.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar streamer: " + ex.getMessage());
            return false;
        }
    }
}
