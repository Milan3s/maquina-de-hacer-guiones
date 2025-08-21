package app.maquina_de_guiones.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa un libro y, además, información del guion al que pertenece
 * y sus preguntas (vía id_guion).
 */
public class Libro {

    // === Campos propios de la tabla libros ===
    private int id;
    private String titulo;
    private String descripcion;
    private int idGuion;
    private LocalDateTime creadoEn; // opcional según tipo en BD

    // === Derivados del JOIN con guiones ===
    private String guionTitulo;
    private String guionContenido; // útil para mostrar “contenido del libro”

    // === Preguntas del guion ===
    private List<Pregunta> preguntas = new ArrayList<>();

    public Libro() {}

    public Libro(int id, String titulo, String descripcion, int idGuion, LocalDateTime creadoEn) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idGuion = idGuion;
        this.creadoEn = creadoEn;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdGuion() { return idGuion; }
    public void setIdGuion(int idGuion) { this.idGuion = idGuion; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public String getGuionTitulo() { return guionTitulo; }
    public void setGuionTitulo(String guionTitulo) { this.guionTitulo = guionTitulo; }

    public String getGuionContenido() { return guionContenido; }
    public void setGuionContenido(String guionContenido) { this.guionContenido = guionContenido; }

    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas != null ? preguntas : new ArrayList<>(); }
    public void addPregunta(Pregunta p) { if (p != null) this.preguntas.add(p); }

    @Override
    public String toString() {
        // Así se verá en el ComboBox
        return titulo != null ? titulo : ("Libro #" + id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return id == libro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
