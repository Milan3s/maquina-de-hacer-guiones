package app.maquina_de_guiones.models;

/**
 * Modelo para representar un guion en el sistema.
 * Cada guion pertenece a un streamer y puede tener asignado un colaborador.
 * 
 * Tabla: guiones
 * Campos: id, titulo, nombre, contenido, descripcion, id_streamer, id_colaborador
 * 
 * @author Milanes
 */
public class Guion {

    private int id;
    private String titulo;
    private String nombre;
    private String contenido;
    private String descripcion;
    private int idStreamer;
    private Integer idColaborador; // puede ser null

    // Constructor completo (para SELECT)
    public Guion(int id, String titulo, String nombre, String contenido, String descripcion, int idStreamer, Integer idColaborador) {
        this.id = id;
        this.titulo = titulo;
        this.nombre = nombre;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.idStreamer = idStreamer;
        this.idColaborador = idColaborador;
    }

    // Constructor para INSERT (sin id)
    public Guion(String titulo, String nombre, String contenido, String descripcion, int idStreamer, Integer idColaborador) {
        this.titulo = titulo;
        this.nombre = nombre;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.idStreamer = idStreamer;
        this.idColaborador = idColaborador;
    }

    // Getters y setters
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

    public int getIdStreamer() {
        return idStreamer;
    }

    public void setIdStreamer(int idStreamer) {
        this.idStreamer = idStreamer;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    @Override
    public String toString() {
        return titulo; // Mostrar título en los ComboBox
    }
}
