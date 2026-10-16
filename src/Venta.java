import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Venta de una o varias boletas de una misma {@link Funcion}, hecha por un {@link Operario}
 * a un {@link Cliente}. Las boletas se agregan después de construirla.
 */
public class Venta {
    private String codigo;              // identificador único de la venta
    private LocalDate fecha;            // día en que se realizó
    private Cliente cliente;            // quien compra
    private Operario operario;          // quien atiende la venta
    private Funcion funcion;            // función a la que pertenecen las boletas
    private ArrayList<Boleta> boletas;  // boletas vendidas (vacía al construir)

    /** Construye la venta con la lista de boletas vacía. */
    public Venta(String codigo, LocalDate fecha, Cliente cliente, Operario operario, Funcion funcion) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.operario = operario;
        this.funcion = funcion;
        this.boletas = new ArrayList<Boleta>();
    }

    /** Añade una boleta a la venta. */
    public void agregarBoleta(Boleta boleta) {
        boletas.add(boleta);
    }

    /** Total de la venta: suma del valor de todas sus boletas (0 si no tiene ninguna). */
    public double calcularTotal() {
        double total = 0;
        for (Boleta boleta : boletas) {
            total += boleta.getValor();
        }
        return total;
    }

    // Getters y setters (la lista de boletas solo tiene getter; se llena con agregarBoleta).
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Operario getOperario() { return operario; }
    public void setOperario(Operario operario) { this.operario = operario; }
    public Funcion getFuncion() { return funcion; }
    public void setFuncion(Funcion funcion) { this.funcion = funcion; }
    public ArrayList<Boleta> getBoletas() { return boletas; }

    /** Resumen: código, fecha, cliente, operario, función, cantidad de boletas y total. */
    @Override
    public String toString() {
        return "Venta{codigo=" + codigo + ", fecha=" + fecha + ", cliente=" + cliente.getNombreCompleto()
                + ", operario=" + operario.getNombreCompleto() + ", funcion=" + funcion.getCodigo()
                + ", boletas=" + boletas.size() + ", total=" + String.format("$%,.0f", calcularTotal()) + "}";
    }
}