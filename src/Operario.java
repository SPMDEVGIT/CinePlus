import java.time.LocalDate;

/**
 * Empleado del cine encargado de atender el mostrador y registrar las ventas.
 * Extiende {@link Persona} con su código de empleado, fecha de contratación y salario.
 */
public class Operario extends Persona {
    private String codigoEmpleado;        // código interno del empleado
    private LocalDate fechaContratacion;  // día en que fue contratado
    private double salarioBasico;         // salario mensual base (>= 0)

    /**
     * Construye un operario. Los cuatro primeros parámetros pertenecen a {@link Persona}
     * y se pasan al constructor padre.
     */
    public Operario(String documento, String nombreCompleto, String correo, String telefono,
                    String codigoEmpleado, LocalDate fechaContratacion, double salarioBasico) {
        super(documento, nombreCompleto, correo, telefono);
        this.codigoEmpleado = codigoEmpleado;
        this.fechaContratacion = fechaContratacion;
        this.salarioBasico = salarioBasico;
    }

    // Getters y setters de los atributos propios del operario.
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public double getSalarioBasico() { return salarioBasico; }
    public void setSalarioBasico(double salarioBasico) { this.salarioBasico = salarioBasico; }

    /** Texto con los datos de la persona más empleado, contratación y salario formateado. */
    @Override
    public String toString() {
        return "Operario{" + super.toString() + ", empleado=" + codigoEmpleado
                + ", contratacion=" + fechaContratacion
                + ", salario=" + String.format("$%,.0f", salarioBasico) + "}";
    }
}