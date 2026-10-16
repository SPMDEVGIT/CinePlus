import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase central del sistema: guarda y administra las salas, películas, funciones, clientes,
 * operarios y ventas del cine, y aplica todas las reglas de negocio (validaciones de
 * duplicados, traslapes de horario y disponibilidad de asientos).
 * Los métodos de registro devuelven true/false y los de búsqueda devuelven el objeto o null.
 */
public class Cine {
    private String nombre;                  // nombre comercial del cine
    private String nit;                     // identificación tributaria
    private String direccion;               // dirección del cine
    private ArrayList<Sala> salas;          // salas registradas
    private ArrayList<Pelicula> peliculas;  // catálogo de películas
    private ArrayList<Funcion> funciones;   // funciones programadas
    private ArrayList<Cliente> clientes;    // clientes registrados
    private ArrayList<Operario> operarios;  // operarios registrados
    private ArrayList<Venta> ventas;        // todas las ventas realizadas

    /** Crea un cine con sus datos y todas las colecciones vacías. */
    public Cine(String nombre, String nit, String direccion) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.salas = new ArrayList<Sala>();
        this.peliculas = new ArrayList<Pelicula>();
        this.funciones = new ArrayList<Funcion>();
        this.clientes = new ArrayList<Cliente>();
        this.operarios = new ArrayList<Operario>();
        this.ventas = new ArrayList<Venta>();
    }

    /**
     * Registra una sala. Devuelve false si es null, si ya existe una sala con ese código
     * o si la capacidad máxima no es igual a filas * columnas.
     */
    public boolean registrarSala(Sala sala) {
        if (sala == null || buscarSala(sala.getCodigo()) != null
                || sala.getCapacidadMaxima() != sala.getFilas() * sala.getColumnas()) {
            return false;
        }
        salas.add(sala);
        return true;
    }

    /** Registra una película. Devuelve false si es null o el código ya existe. */
    public boolean registrarPelicula(Pelicula pelicula) {
        if (pelicula == null || buscarPelicula(pelicula.getCodigo()) != null) {
            return false;
        }
        peliculas.add(pelicula);
        return true;
    }

    /**
     * Registra un cliente. Devuelve false si es null o si su documento ya está
     * en uso por otro cliente u operario.
     */
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || documentoExiste(cliente.getDocumento())) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    /**
     * Registra un operario. Aplica la misma validación de documento que
     * {@link #registrarCliente(Cliente)}.
     */
    public boolean registrarOperario(Operario operario) {
        if (operario == null || documentoExiste(operario.getDocumento())) {
            return false;
        }
        operarios.add(operario);
        return true;
    }

    /**
     * Programa una función. Devuelve false si es null, si le falta la película o la sala,
     * si el código ya existe o si se traslapa en horario con alguna función ya registrada.
     */
    public boolean programarFuncion(Funcion funcion) {
        if (funcion == null || funcion.getPelicula() == null || funcion.getSala() == null
                || buscarFuncion(funcion.getCodigo()) != null) {
            return false;
        }
        for (Funcion existente : funciones) {
            if (funcion.seTraslapaCon(existente)) {
                return false;
            }
        }
        funciones.add(funcion);
        return true;
    }

    /**
     * Realiza una venta de boletas. Primero valida TODO sin modificar nada; devuelve null si:
     * el código de venta existe; el cliente, operario o función no existen; los arreglos son
     * null, vacíos o de distinto tamaño; algún asiento está fuera del rango de la sala,
     * repetido en la solicitud o ya ocupado en la función.
     * Si todo es válido crea la venta, una boleta por asiento (código ventaCodigo-B1, -B2...,
     * con el valor calculado para el cliente) y la registra en el cine y en la función.
     *
     * @param filas    fila de cada asiento solicitado
     * @param asientos número de asiento de cada solicitud (misma posición que en filas)
     */
    public Venta realizarVenta(String codigo, LocalDate fecha, String documentoCliente,
                               String documentoOperario, String codigoFuncion,
                               int[] filas, int[] asientos) {
        // Validaciones de existencia
        if (buscarVenta(codigo) != null) {
            return null;
        }
        Cliente cliente = buscarCliente(documentoCliente);
        Operario operario = buscarOperario(documentoOperario);
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (cliente == null || operario == null || funcion == null) {
            return null;
        }
        // Validaciones de los arreglos de asientos
        if (filas == null || asientos == null || filas.length == 0 || filas.length != asientos.length) {
            return null;
        }
        Sala sala = funcion.getSala();
        for (int i = 0; i < filas.length; i++) {
            // Rango dentro de la sala
            if (filas[i] < 1 || filas[i] > sala.getFilas()
                    || asientos[i] < 1 || asientos[i] > sala.getColumnas()) {
                return null;
            }
            // Ya vendido en esta función
            if (funcion.estaOcupado(filas[i], asientos[i])) {
                return null;
            }
            // Repetido dentro de la misma solicitud
            for (int j = 0; j < i; j++) {
                if (filas[j] == filas[i] && asientos[j] == asientos[i]) {
                    return null;
                }
            }
        }
        // Todo es válido: ahora sí se crean y registran los objetos
        Venta venta = new Venta(codigo, fecha, cliente, operario, funcion);
        double valor = funcion.calcularValorBoleta(cliente);
        for (int i = 0; i < filas.length; i++) {
            venta.agregarBoleta(new Boleta(codigo + "-B" + (i + 1), filas[i], asientos[i], valor));
        }
        ventas.add(venta);
        funcion.getVentas().add(venta);
        return venta;
    }

    /** Total de la venta indicada, o -1 si no existe. */
    public double calcularTotalVenta(String codigoVenta) {
        Venta venta = buscarVenta(codigoVenta);
        if (venta == null) {
            return -1;
        }
        return venta.calcularTotal();
    }

    /** Porcentaje de ocupación de la función indicada, o -1 si no existe. */
    public double calcularOcupacionFuncion(String codigoFuncion) {
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (funcion == null) {
            return -1;
        }
        return funcion.calcularOcupacion();
    }

    /** Busca una sala por código; devuelve null si no existe. */
    public Sala buscarSala(String codigo) {
        for (Sala sala : salas) {
            if (sala.getCodigo().equals(codigo)) {
                return sala;
            }
        }
        return null;
    }

    /** Busca una película por código; devuelve null si no existe. */
    public Pelicula buscarPelicula(String codigo) {
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo().equals(codigo)) {
                return pelicula;
            }
        }
        return null;
    }

    /** Busca una función por código; devuelve null si no existe. */
    public Funcion buscarFuncion(String codigo) {
        for (Funcion funcion : funciones) {
            if (funcion.getCodigo().equals(codigo)) {
                return funcion;
            }
        }
        return null;
    }

    /** Busca un cliente por documento; devuelve null si no existe. */
    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    /** Busca un operario por documento; devuelve null si no existe. */
    public Operario buscarOperario(String documento) {
        for (Operario operario : operarios) {
            if (operario.getDocumento().equals(documento)) {
                return operario;
            }
        }
        return null;
    }

    /** Busca una venta por código; devuelve null si no existe. */
    public Venta buscarVenta(String codigo) {
        for (Venta venta : ventas) {
            if (venta.getCodigo().equals(codigo)) {
                return venta;
            }
        }
        return null;
    }

    /**
     * Método privado reutilizado por registrarCliente y registrarOperario: un documento
     * no puede repetirse ni entre clientes ni entre operarios.
     */
    private boolean documentoExiste(String documento) {
        return buscarCliente(documento) != null || buscarOperario(documento) != null;
    }

    // Getters y setters (las listas solo tienen getter; se modifican con los métodos de registro).
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public ArrayList<Sala> getSalas() { return salas; }
    public ArrayList<Pelicula> getPeliculas() { return peliculas; }
    public ArrayList<Funcion> getFunciones() { return funciones; }
    public ArrayList<Cliente> getClientes() { return clientes; }
    public ArrayList<Operario> getOperarios() { return operarios; }
    public ArrayList<Venta> getVentas() { return ventas; }

    /** Resumen del cine con la cantidad de elementos de cada colección. */
    @Override
    public String toString() {
        return "Cine{nombre=" + nombre + ", nit=" + nit + ", direccion=" + direccion
                + ", salas=" + salas.size() + ", peliculas=" + peliculas.size()
                + ", funciones=" + funciones.size() + ", clientes=" + clientes.size()
                + ", operarios=" + operarios.size() + ", ventas=" + ventas.size() + "}";
    }
}
