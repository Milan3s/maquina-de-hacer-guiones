package app.maquina_de_guiones.DAO;

import app.maquina_de_guiones.models.Colaborador;
import app.maquina_de_guiones.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CRUD para la entidad Colaboradores.
 * Permite operaciones básicas: listar, insertar, actualizar y eliminar.
 * 
 * @author Milanes
 */
public class Crud_Colaboradores {

    /**
     * Obtener todos los colaboradores para mostrar en ComboBox o tabla.
     */
    public static ArrayList<Colaborador> obtenerColaboradores() {
        ArrayList<Colaborador> lista = new ArrayList<>();
        String sql = "SELECT * FROM colaboradores";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return lista;

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Colaborador c = new Colaborador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("canal")
                    );
                    lista.add(c);
                }
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al obtener colaboradores: " + ex.getMessage());
        }

        return lista;
    }

    /**
     * Insertar un nuevo colaborador en la base de datos.
     */
    public static boolean insertarColaborador(Colaborador c) {
        String sql = "INSERT INTO colaboradores (nombre, descripcion, canal) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getNombre());
                ps.setString(2, c.getDescripcion());
                ps.setString(3, c.getCanal());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Colaborador insertado correctamente." : "⚠️ No se insertó el colaborador.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar colaborador: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Actualizar un colaborador existente por ID.
     */
    public static boolean actualizarColaborador(Colaborador c) {
        String sql = "UPDATE colaboradores SET nombre = ?, descripcion = ?, canal = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getNombre());
                ps.setString(2, c.getDescripcion());
                ps.setString(3, c.getCanal());
                ps.setInt(4, c.getId());

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Colaborador actualizado correctamente." : "⚠️ No se actualizó el colaborador.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar colaborador: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Eliminar un colaborador por ID.
     */
    public static boolean eliminarColaborador(int id) {
        String sql = "DELETE FROM colaboradores WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                boolean exito = ps.executeUpdate() > 0;
                System.out.println(exito ? "✅ Colaborador eliminado correctamente." : "⚠️ No se eliminó el colaborador.");
                return exito;
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar colaborador: " + ex.getMessage());
            return false;
        }
    }
}
