package app.maquina_de_guiones.models;

/**
 * Modelo para representar una pregunta en el sistema. Cada pregunta está
 * vinculada a un guion mediante id_guion.
 *
 * Tabla: preguntas Campos: id, titulo, nombre, contenido, descripcion,
 * observaciones, id_guion
 *
 * @author Milanes
 */
public class Pregunta {

    private int id;
    private String titulo;
    private String nombre;
    private String contenido;
    private String descripcion;
    private String observaciones; // puede ser null
    private int idGuion;

    // Constructor completo
    public Pregunta(int id, String titulo, String nombre, String contenido, String descripcion, String observaciones, int idGuion) {
        this.id = id;
        this.titulo = titulo;
        this.nombre = nombre;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.idGuion = idGuion;
    }

    // Constructor sin ID (para insertar)
    public Pregunta(String titulo, String nombre, String contenido, String descripcion, String observaciones, int idGuion) {
        this.titulo = titulo;
        this.nombre = nombre;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.idGuion = idGuion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdGuion() {
        return idGuion;
    }

    public void setIdGuion(int idGuion) {
        this.idGuion = idGuion;
    }

    @Override
    public String toString() {
        return titulo; // o nombre, según lo que quieras mostrar
    }

}
