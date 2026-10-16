/**
 * Boleta correspondiente a un único asiento dentro de una {@link Venta}.
 * El valor ya incluye el recargo de la sala y el descuento del cliente
 * (se calcula con {@link Funcion#calcularValorBoleta(Cliente)} al crear la venta).
 */
public class Boleta {
    private String codigo;   // código de la venta + "-B" + consecutivo (ej: V1-B2)
    private int fila;        // fila del asiento (desde 1)
    private int asiento;     // número de asiento dentro de la fila (desde 1)
    private double valor;    // valor final cobrado por esta boleta

    /** Construye una boleta con su código, ubicación del asiento y valor. */
    public Boleta(String codigo, int fila, int asiento, double valor) {
        this.codigo = codigo;
        this.fila = fila;
        this.asiento = asiento;
        this.valor = valor;
    }

    // Getters y setters de los atributos de la boleta.
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }
    public int getAsiento() { return asiento; }
    public void setAsiento(int asiento) { this.asiento = asiento; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    /** Resumen de la boleta con el valor en formato de dinero. */
    @Override
    public String toString() {
        return "Boleta{codigo=" + codigo + ", fila=" + fila + ", asiento=" + asiento
                + ", valor=" + String.format("$%,.0f", valor) + "}";
    }
}
