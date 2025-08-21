package app.maquina_de_guiones.DAO;

import app.maquina_de_guiones.models.Pregunta;
import app.maquina_de_guiones.ConexionDB;

import java.sql.*;
import java.util.ArrayList;

public class Crud_Preguntas {

    // INSERTAR
    public static boolean insertarPregunta(Pregunta p) {
        String sql = "INSERT INTO preguntas (titulo, nombre, contenido, descripcion, observaciones, id_guion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getTitulo());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getContenido());
                ps.setString(4, p.getDescripcion());
                ps.setString(5, p.getObservaciones());
                ps.setInt(6, p.getIdGuion());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Pregunta insertada correctamente." : "⚠️ No se insertó la pregunta.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar pregunta: " + ex.getMessage());
            return false;
        }
    }

    // ACTUALIZAR
    public static boolean actualizarPregunta(Pregunta p) {
        String sql = "UPDATE preguntas SET titulo = ?, nombre = ?, contenido = ?, descripcion = ?, observaciones = ?, id_guion = ? "
                + "WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getTitulo());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getContenido());
                ps.setString(4, p.getDescripcion());
                ps.setString(5, p.getObservaciones());
                ps.setInt(6, p.getIdGuion());
                ps.setInt(7, p.getId());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Pregunta actualizada correctamente." : "⚠️ No se actualizó la pregunta.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar pregunta: " + ex.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public static boolean eliminarPregunta(int id) {
        String sql = "DELETE FROM preguntas WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Pregunta eliminada correctamente." : "⚠️ No se eliminó la pregunta.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar pregunta: " + ex.getMessage());
            return false;
        }
    }

    // OBTENER TODAS LAS PREGUNTAS
    public static ArrayList<Pregunta> obtenerPreguntas() {
        ArrayList<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pregunta p = new Pregunta(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("nombre"),
                        rs.getString("contenido"),
                        rs.getString("descripcion"),
                        rs.getString("observaciones"),
                        rs.getInt("id_guion")
                );
                lista.add(p);
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener preguntas: " + ex.getMessage());
        }

        return lista;
    }

    // OBTENER PREGUNTAS POR GUION
    public static ArrayList<Pregunta> obtenerPreguntasPorGuion(int idGuion) {
        ArrayList<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas WHERE id_guion = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGuion);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pregunta p = new Pregunta(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("nombre"),
                            rs.getString("contenido"),
                            rs.getString("descripcion"),
                            rs.getString("observaciones"),
                            rs.getInt("id_guion")
                    );
                    lista.add(p);
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener preguntas por guion: " + ex.getMessage());
        }

        return lista;
    }
    // OBTENER UNA PREGUNTA POR GUION (la primera que coincida)

    public static Pregunta obtenerPreguntaPorGuion(int idGuion) {
        String sql = "SELECT * FROM preguntas WHERE id_guion = ? LIMIT 1";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGuion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pregunta(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("nombre"),
                            rs.getString("contenido"),
                            rs.getString("descripcion"),
                            rs.getString("observaciones"),
                            rs.getInt("id_guion")
                    );
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener pregunta por guion: " + ex.getMessage());
        }

        return null;
    }

}
