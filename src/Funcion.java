import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Función: proyección de una {@link Pelicula} en una {@link Sala} en una fecha y hora dadas.
 * Guarda las {@link Venta}s realizadas para ella, de las que se deducen los asientos ocupados
 * y el porcentaje de ocupación.
 */
public class Funcion {
    private String codigo;            // identificador único de la función
    private LocalDate fecha;          // día de la proyección
    private LocalTime horaInicio;     // hora en que empieza
    private double precioBase;        // precio de la boleta antes de recargo y descuento
    private Pelicula pelicula;        // película que se proyecta
    private Sala sala;                // sala donde se proyecta
    private ArrayList<Venta> ventas;  // ventas asociadas (vacía al construir)

    /** Construye la función con la lista de ventas vacía. */
    public Funcion(String codigo, LocalDate fecha, LocalTime horaInicio, double precioBase,
                   Pelicula pelicula, Sala sala) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.precioBase = precioBase;
        this.pelicula = pelicula;
        this.sala = sala;
        this.ventas = new ArrayList<Venta>();
    }

    /** Hora de fin: hora de inicio más la duración de la película en minutos. */
    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(pelicula.getDuracionMinutos());
    }

    /**
     * Indica si esta función choca en horario con otra.
     * Solo hay conflicto si es la misma sala, la misma fecha y los intervalos se cruzan:
     * inicioA &lt; finB y inicioB &lt; finA. Si una termina justo cuando empieza la otra
     * NO se considera conflicto. Se usa LocalDateTime para comparar fecha y hora juntas.
     */
    public boolean seTraslapaCon(Funcion otra) {
        if (otra == null || !sala.getCodigo().equals(otra.sala.getCodigo())
                || !fecha.equals(otra.fecha)) {
            return false;
        }
        LocalDateTime inicioA = fecha.atTime(horaInicio);
        LocalDateTime finA = inicioA.plusMinutes(pelicula.getDuracionMinutos());
        LocalDateTime inicioB = otra.fecha.atTime(otra.horaInicio);
        LocalDateTime finB = inicioB.plusMinutes(otra.pelicula.getDuracionMinutos());
        return inicioA.isBefore(finB) && inicioB.isBefore(finA);
    }

    /**
     * Revisa todas las boletas de todas las ventas de esta función y devuelve true
     * si alguna ya tiene la fila y el asiento indicados.
     */
    public boolean estaOcupado(int fila, int asiento) {
        for (Venta venta : ventas) {
            for (Boleta boleta : venta.getBoletas()) {
                if (boleta.getFila() == fila && boleta.getAsiento() == asiento) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Cuenta las boletas vendidas sumando las de todas las ventas de la función. */
    public int contarBoletasVendidas() {
        int total = 0;
        for (Venta venta : ventas) {
            total += venta.getBoletas().size();
        }
        return total;
    }

    /** Ocupación en porcentaje (0 a 100): boletas vendidas sobre la capacidad de la sala. */
    public double calcularOcupacion() {
        return contarBoletasVendidas() * 100.0 / sala.getCapacidadMaxima();
    }

    /**
     * Valor de una boleta para el cliente dado:
     * precioBase * (1 + recargo de la sala) * (1 - descuento/100).
     * El descuento se aplica DESPUÉS del recargo.
     */
    public double calcularValorBoleta(Cliente cliente) {
        return precioBase * (1 + sala.calcularRecargo()) * (1 - cliente.getPorcentajeDescuento() / 100);
    }

    // Getters y setters (la lista de ventas solo tiene getter; se modifica desde Cine).
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }
    public ArrayList<Venta> getVentas() { return ventas; }

    /** Resumen: código, fecha, horario inicio-fin, precio, película, sala y número de ventas. */
    @Override
    public String toString() {
        return "Funcion{codigo=" + codigo + ", fecha=" + fecha + ", " + horaInicio + "-" + calcularHoraFin()
                + ", precioBase=" + String.format("$%,.0f", precioBase)
                + ", pelicula=" + pelicula.getTitulo() + ", sala=" + sala.getNombre()
                + ", ventas=" + ventas.size() + "}";
    }
}

