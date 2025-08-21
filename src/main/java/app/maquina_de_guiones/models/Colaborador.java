package app.maquina_de_guiones.models;

/**
 * Modelo de datos para un Colaborador.
 * Corresponde a la tabla `colaboradores` en la base de datos.
 * 
 * Campos: id, nombre, descripcion, canal
 * 
 * @author Milanes
 */
public class Colaborador {

    private int id;
    private String nombre;
    private String descripcion;
    private String canal;

    // Constructores

    public Colaborador() {}

    public Colaborador(int id, String nombre, String descripcion, String canal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.canal = canal;
    }

    public Colaborador(String nombre, String descripcion, String canal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.canal = canal;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    // Representación en ComboBox

    @Override
    public String toString() {
        return nombre;
    }
}
