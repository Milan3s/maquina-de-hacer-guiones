package app.maquina_de_guiones.DAO;

import app.maquina_de_guiones.ConexionDB;
import app.maquina_de_guiones.models.Libro;
import app.maquina_de_guiones.models.Pregunta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * CRUD/DAO de Libros. Incluye lecturas con JOIN a guiones y preguntas.
 */
public class Crud_Libros {

    // ===================== LECTURAS =====================

    public static List<Libro> listarLibrosConGuionYPreguntas() {
        String sql =
            "SELECT " +
            "  l.id            AS l_id, " +
            "  l.titulo        AS l_titulo, " +
            "  l.descripcion   AS l_desc, " +
            "  l.id_guion      AS l_id_guion, " +
            "  l.creado_en     AS l_creado, " +
            "  g.titulo        AS g_titulo, " +
            "  g.contenido     AS g_contenido, " +
            "  p.id            AS p_id, " +
            "  p.titulo        AS p_titulo, " +
            "  p.nombre        AS p_nombre, " +
            "  p.contenido     AS p_contenido, " +
            "  p.descripcion   AS p_desc, " +
            "  p.observaciones AS p_obs " +
            "FROM libros l " +
            "JOIN guiones g ON g.id = l.id_guion " +
            "LEFT JOIN preguntas p ON p.id_guion = g.id " +
            "ORDER BY l.id, p.id";

        Map<Integer, Libro> porId = new LinkedHashMap<>();

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int lId = rs.getInt("l_id");
                Libro libro = porId.get(lId);
                if (libro == null) {
                    libro = mapearLibroBaseDesdeJoin(rs);
                    libro.setGuionTitulo(rs.getString("g_titulo"));
                    libro.setGuionContenido(rs.getString("g_contenido"));
                    porId.put(lId, libro);
                }
                agregarPreguntaSiExiste(rs, libro);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al listar libros con guion y preguntas: " + ex.getMessage());
        }

        return new ArrayList<>(porId.values());
    }

    public static Libro obtenerPorId(int idLibro) {
        String sql =
            "SELECT " +
            "  l.id            AS l_id, " +
            "  l.titulo        AS l_titulo, " +
            "  l.descripcion   AS l_desc, " +
            "  l.id_guion      AS l_id_guion, " +
            "  l.creado_en     AS l_creado, " +
            "  g.titulo        AS g_titulo, " +
            "  g.contenido     AS g_contenido, " +
            "  p.id            AS p_id, " +
            "  p.titulo        AS p_titulo, " +
            "  p.nombre        AS p_nombre, " +
            "  p.contenido     AS p_contenido, " +
            "  p.descripcion   AS p_desc, " +
            "  p.observaciones AS p_obs " +
            "FROM libros l " +
            "JOIN guiones g ON g.id = l.id_guion " +
            "LEFT JOIN preguntas p ON p.id_guion = g.id " +
            "WHERE l.id = ? " +
            "ORDER BY p.id";

        Libro libro = null;

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (libro == null) {
                        libro = mapearLibroBaseDesdeJoin(rs);
                        libro.setGuionTitulo(rs.getString("g_titulo"));
                        libro.setGuionContenido(rs.getString("g_contenido"));
                    }
                    agregarPreguntaSiExiste(rs, libro);
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener libro por id: " + ex.getMessage());
        }
        return libro;
    }

    public static List<Libro> listar() {
        String sql = "SELECT id, titulo, descripcion, id_guion, creado_en FROM libros ORDER BY id";
        List<Libro> out = new ArrayList<>();

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro l = new Libro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setDescripcion(rs.getString("descripcion"));
                // id_guion puede ser NULL => lo representamos como 0
                Integer idGuionObj = (Integer) rs.getObject("id_guion");
                l.setIdGuion(idGuionObj != null ? idGuionObj : 0);
                Timestamp ts = rs.getTimestamp("creado_en");
                l.setCreadoEn(ts != null ? ts.toLocalDateTime() : null);
                out.add(l);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al listar libros: " + ex.getMessage());
        }
        return out;
    }

    // ===================== ESCRITURAS =====================

    /** Inserta un libro. Si creado_en es null, se omite la columna para usar el DEFAULT de MySQL. */
    public static boolean insertar(Libro l) {
        boolean conFecha = l.getCreadoEn() != null;

        String sql = conFecha
                ? "INSERT INTO libros (titulo, descripcion, id_guion, creado_en) VALUES (?, ?, ?, ?)"
                : "INSERT INTO libros (titulo, descripcion, id_guion) VALUES (?, ?, ?)";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            ps.setString(i++, l.getTitulo());
            ps.setString(i++, l.getDescripcion());

            int idGuion = l.getIdGuion(); // primitivo
            if (idGuion > 0) {
                ps.setInt(i++, idGuion);
            } else {
                ps.setNull(i++, Types.INTEGER);
            }

            if (conFecha) {
                ps.setTimestamp(i, Timestamp.valueOf(l.getCreadoEn()));
            }

            boolean exito = ps.executeUpdate() > 0;

            if (exito) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) l.setId(rs.getInt(1));
                }
            }
            System.out.println(exito ? "✅ Libro insertado correctamente." : "⚠️ No se insertó el libro.");
            return exito;

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar libro: " + ex.getMessage());
            return false;
        }
    }

    /** Actualiza un libro. Si creado_en es null, NO se modifica ese campo. */
    public static boolean actualizar(Libro l) {
        if (l.getId() <= 0) {
            System.err.println("❌ Error al actualizar libro: ID inválido (<= 0).");
            return false;
        }

        boolean conFecha = l.getCreadoEn() != null;

        String sql = conFecha
                ? "UPDATE libros SET titulo = ?, descripcion = ?, id_guion = ?, creado_en = ? WHERE id = ?"
                : "UPDATE libros SET titulo = ?, descripcion = ?, id_guion = ? WHERE id = ?";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, l.getTitulo());
            ps.setString(i++, l.getDescripcion());

            int idGuion = l.getIdGuion();
            if (idGuion > 0) {
                ps.setInt(i++, idGuion);
            } else {
                ps.setNull(i++, Types.INTEGER);
            }

            if (conFecha) {
                ps.setTimestamp(i++, Timestamp.valueOf(l.getCreadoEn()));
            }

            ps.setInt(i, l.getId());

            boolean exito = ps.executeUpdate() > 0;
            System.out.println(exito ? "✅ Libro actualizado correctamente." : "⚠️ No se actualizó el libro.");
            return exito;

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar libro: " + ex.getMessage());
            return false;
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean exito = ps.executeUpdate() > 0;
            System.out.println(exito ? "✅ Libro eliminado correctamente." : "⚠️ No se eliminó el libro.");
            return exito;

        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar libro: " + ex.getMessage());
            return false;
        }
    }

    // ===================== Helpers =====================

    private static Libro mapearLibroBaseDesdeJoin(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getInt("l_id"));
        l.setTitulo(rs.getString("l_titulo"));
        l.setDescripcion(rs.getString("l_desc"));

        Integer idGuionObj = (Integer) rs.getObject("l_id_guion");
        l.setIdGuion(idGuionObj != null ? idGuionObj : 0);

        Timestamp ts = rs.getTimestamp("l_creado");
        l.setCreadoEn(ts != null ? ts.toLocalDateTime() : null);
        return l;
    }

    private static void agregarPreguntaSiExiste(ResultSet rs, Libro libro) throws SQLException {
        Integer pId = (Integer) rs.getObject("p_id");
        if (pId == null) return;

        Pregunta p = new Pregunta(
                pId,
                rs.getString("p_titulo"),
                rs.getString("p_nombre"),
                rs.getString("p_contenido"),
                rs.getString("p_desc"),
                rs.getString("p_obs"),
                libro.getIdGuion()
        );

        libro.addPregunta(p);
    }

    // Util opcional
    @SuppressWarnings("unused")
    private static LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }
}
