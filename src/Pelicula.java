/**
 * Película del catálogo del cine. Su duración se usa para calcular la hora de fin de una
 * {@link Funcion} y detectar traslapes de horario en una misma sala.
 */
public class Pelicula {
    private String codigo;            // identificador único de la película
    private String titulo;            // título para mostrar
    private int duracionMinutos;      // duración en minutos (> 0)
    private Genero genero;            // género de la película
    private int clasificacionEdad;    // edad mínima recomendada (0 = apta para todo público)

    /** Construye una película con todos sus datos. */
    public Pelicula(String codigo, String titulo, int duracionMinutos, Genero genero, int clasificacionEdad) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.clasificacionEdad = clasificacionEdad;
    }

    // Getters y setters de los atributos de la película.
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }
    public int getClasificacionEdad() { return clasificacionEdad; }
    public void setClasificacionEdad(int clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }

    /** Resumen de la película: código, título, duración, género y edad mínima. */
    @Override
    public String toString() {
        return "Pelicula{codigo=" + codigo + ", titulo=" + titulo + ", duracion=" + duracionMinutos
                + " min, genero=" + genero + ", edad=" + clasificacionEdad + "+}";
    }
}
