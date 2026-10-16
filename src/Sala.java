/**
 * Sala de cine. Sus asientos forman una cuadrícula de filas x columnas, de modo que
 * la capacidad máxima debe ser igual a filas * columnas (lo valida {@link Cine#registrarSala(Sala)}).
 * Los asientos se numeran desde 1: fila 1..filas y asiento 1..columnas.
 */
public class Sala {
    private String codigo;          // identificador único de la sala
    private String nombre;          // nombre para mostrar
    private int capacidadMaxima;    // total de asientos
    private TipoSala tipo;          // tradicional, VIP o 3D
    private int filas;              // cantidad de filas de asientos
    private int columnas;           // asientos por fila

    /** Construye una sala con todos sus datos; no valida la capacidad (lo hace el Cine). */
    public Sala(String codigo, String nombre, int capacidadMaxima, TipoSala tipo, int filas, int columnas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.tipo = tipo;
        this.filas = filas;
        this.columnas = columnas;
    }

    /**
     * Recargo que se suma al precio base según el tipo de sala, como fracción:
     * TRADICIONAL 0.0, VIP 0.30 (30%) y SALA_3D 0.20 (20%).
     */
    public double calcularRecargo() {
        if (tipo == TipoSala.VIP) {
            return 0.30;
        }
        if (tipo == TipoSala.SALA_3D) {
            return 0.20;
        }
        return 0.0;
    }

    // Getters y setters de los atributos de la sala.
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public TipoSala getTipo() { return tipo; }
    public void setTipo(TipoSala tipo) { this.tipo = tipo; }
    public int getFilas() { return filas; }
    public void setFilas(int filas) { this.filas = filas; }
    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }

    /** Resumen de la sala: código, nombre, tipo y capacidad con su distribución. */
    @Override
    public String toString() {
        return "Sala{codigo=" + codigo + ", nombre=" + nombre + ", tipo=" + tipo
                + ", capacidad=" + capacidadMaxima + " (" + filas + "x" + columnas + ")}";
    }
}