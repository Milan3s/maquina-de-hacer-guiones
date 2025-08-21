package app.maquina_de_guiones.DAO;

import app.maquina_de_guiones.models.Guion;
import app.maquina_de_guiones.ConexionDB;

import java.sql.*;
import java.util.ArrayList;

public class Crud_Guiones {

    // INSERTAR
    public static boolean insertarGuion(Guion g) {
        String sql = "INSERT INTO guiones (titulo, nombre, contenido, descripcion, id_streamer, id_colaborador) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, g.getTitulo());
                ps.setString(2, g.getNombre());
                ps.setString(3, g.getContenido());
                ps.setString(4, g.getDescripcion());
                ps.setInt(5, g.getIdStreamer());

                if (g.getIdColaborador() != null) {
                    ps.setInt(6, g.getIdColaborador());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Guion insertado correctamente." : "⚠️ No se insertó el guion.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar guion: " + ex.getMessage());
            return false;
        }
    }

    // OBTENER TODOS
    public static ArrayList<Guion> obtenerGuiones() {
        ArrayList<Guion> lista = new ArrayList<>();
        String sql = "SELECT * FROM guiones";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return lista;

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Guion g = new Guion(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("nombre"),
                            rs.getString("contenido"),
                            rs.getString("descripcion"),
                            rs.getInt("id_streamer"),
                            rs.getObject("id_colaborador") != null ? rs.getInt("id_colaborador") : null
                    );
                    lista.add(g);
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener guiones: " + ex.getMessage());
        }

        return lista;
    }

    // ACTUALIZAR
    public static boolean actualizarGuion(Guion g) {
        String sql = "UPDATE guiones SET titulo = ?, nombre = ?, contenido = ?, descripcion = ?, id_streamer = ?, id_colaborador = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, g.getTitulo());
                ps.setString(2, g.getNombre());
                ps.setString(3, g.getContenido());
                ps.setString(4, g.getDescripcion());
                ps.setInt(5, g.getIdStreamer());

                if (g.getIdColaborador() != null) {
                    ps.setInt(6, g.getIdColaborador());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                ps.setInt(7, g.getId());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Guion actualizado correctamente." : "⚠️ No se actualizó el guion.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar guion: " + ex.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public static boolean eliminarGuion(int id) {
        String sql = "DELETE FROM guiones WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Guion eliminado correctamente." : "⚠️ No se eliminó el guion.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar guion: " + ex.getMessage());
            return false;
        }
    }

    // OBTENER POR COLABORADOR
    public static Guion obtenerGuionPorColaborador(int idColaborador) {
        String sql = "SELECT * FROM guiones WHERE id_colaborador = ? LIMIT 1";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return null;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idColaborador);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Guion(
                                rs.getInt("id"),
                                rs.getString("titulo"),
                                rs.getString("nombre"),
                                rs.getString("contenido"),
                                rs.getString("descripcion"),
                                rs.getInt("id_streamer"),
                                rs.getObject("id_colaborador") != null ? rs.getInt("id_colaborador") : null
                        );
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al recuperar guion del colaborador: " + ex.getMessage());
        }

        return null;
    }

    // OBTENER POR STREAMER
    public static Guion obtenerGuionPorStreamer(int idStreamer) {
        String sql = "SELECT * FROM guiones WHERE id_streamer = ? LIMIT 1";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return null;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idStreamer);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Guion(
                                rs.getInt("id"),
                                rs.getString("titulo"),
                                rs.getString("nombre"),
                                rs.getString("contenido"),
                                rs.getString("descripcion"),
                                rs.getInt("id_streamer"),
                                rs.getObject("id_colaborador") != null ? rs.getInt("id_colaborador") : null
                        );
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al recuperar guion del streamer: " + ex.getMessage());
        }

        return null;
    }
}
