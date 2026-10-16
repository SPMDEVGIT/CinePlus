/**
 * Clase abstracta con los datos comunes de cualquier persona del sistema.
 * No se puede instanciar directamente: se usa a través de {@link Cliente} y {@link Operario}.
 * El documento identifica de forma única a la persona dentro del {@link Cine}.
 */
public abstract class Persona {
    private String documento;       // identificación única (no se repite entre clientes y operarios)
    private String nombreCompleto;  // nombre y apellidos
    private String correo;          // correo electrónico de contacto
    private String telefono;        // teléfono de contacto

    /**
     * Construye una persona con todos sus datos básicos.
     * Lo invocan los constructores de las subclases mediante super(...).
     */
    public Persona(String documento, String nombreCompleto, String correo, String telefono) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Getters y setters: leen y modifican cada atributo privado sin lógica adicional.
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /** Devuelve los datos comunes en texto; las subclases lo reutilizan y le añaden los suyos. */
    @Override
    public String toString() {
        return "documento=" + documento + ", nombre=" + nombreCompleto
                + ", correo=" + correo + ", telefono=" + telefono;
    }
}