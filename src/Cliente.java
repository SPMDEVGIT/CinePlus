/**
 * Cliente afiliado al cine. Además de los datos de {@link Persona}, tiene un código de
 * afiliación y un porcentaje de descuento que se aplica al valor de cada boleta que compra
 * (ver {@link Funcion#calcularValorBoleta(Cliente)}).
 */
public class Cliente extends Persona {
    private String codigoAfiliacion;      // código del programa de afiliados
    private double porcentajeDescuento;   // descuento de 0 a 100 (0 = sin descuento)

    /**
     * Construye un cliente. Los cuatro primeros parámetros pertenecen a {@link Persona}
     * y se pasan al constructor padre.
     */
    public Cliente(String documento, String nombreCompleto, String correo, String telefono,
                   String codigoAfiliacion, double porcentajeDescuento) {
        super(documento, nombreCompleto, correo, telefono);
        this.codigoAfiliacion = codigoAfiliacion;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    // Getters y setters de los atributos propios del cliente.
    public String getCodigoAfiliacion() { return codigoAfiliacion; }
    public void setCodigoAfiliacion(String codigoAfiliacion) { this.codigoAfiliacion = codigoAfiliacion; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    /** Texto con los datos de la persona más afiliación y descuento (con dos decimales). */
    @Override
    public String toString() {
        return "Cliente{" + super.toString() + ", afiliacion=" + codigoAfiliacion
                + ", descuento=" + String.format("%.2f%%", porcentajeDescuento) + "}";
    }
}

