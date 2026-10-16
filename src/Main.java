import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Interfaz de usuario del sistema CinePlus, 100% con ventanas JOptionPane.
 * Contiene el menú principal, un método por cada opción y los métodos auxiliares de lectura
 * (leerTexto, leerEntero, leerDouble, leerFecha, leerHora). Todos los auxiliares devuelven
 * null cuando el usuario cancela; en ese caso la opción se aborta y se vuelve al menú.
 * Aquí se validan los datos de entrada; las reglas de negocio las aplica {@link Cine}.
 */
public class Main {
    private static final String TITULO = "CinePlus";

    /**
     * Punto de entrada: crea el cine y repite el menú hasta que el usuario elige 0
     * o cancela/cierra el diálogo.
     */
    public static void main(String[] args) {
        Cine cine = new Cine("CinePlus", "900123456-7", "Calle 20 # 14-35, Armenia, Quindío");
        boolean salir = false;
        while (!salir) {
            String opcion = JOptionPane.showInputDialog(null,
                    "=== " + cine.getNombre() + " ===\n"
                            + "1. Registrar sala\n"
                            + "2. Registrar película\n"
                            + "3. Registrar cliente\n"
                            + "4. Registrar operario\n"
                            + "5. Programar función\n"
                            + "6. Realizar venta de boletas\n"
                            + "7. Calcular total de una venta\n"
                            + "8. Calcular ocupación de una función\n"
                            + "9. Listar películas, funciones y salas\n"
                            + "10. Cargar datos de ejemplo\n"
                            + "0. Salir\n\nElija una opción:",
                    TITULO, JOptionPane.QUESTION_MESSAGE);
            if (opcion == null) {
                break; // cancelar o cerrar el diálogo sale
            }
            opcion = opcion.trim();
            if (opcion.equals("1")) {
                registrarSala(cine);
            } else if (opcion.equals("2")) {
                registrarPelicula(cine);
            } else if (opcion.equals("3")) {
                registrarCliente(cine);
            } else if (opcion.equals("4")) {
                registrarOperario(cine);
            } else if (opcion.equals("5")) {
                programarFuncion(cine);
            } else if (opcion.equals("6")) {
                realizarVenta(cine);
            } else if (opcion.equals("7")) {
                calcularTotalVenta(cine);
            } else if (opcion.equals("8")) {
                calcularOcupacion(cine);
            } else if (opcion.equals("9")) {
                listar(cine);
            } else if (opcion.equals("10")) {
                cargarDatosEjemplo(cine);
            } else if (opcion.equals("0")) {
                salir = true;
            } else {
                mostrar("Opción no válida.");
            }
        }
        mostrar("Gracias por usar CinePlus.");
    }

    // ---------- Opciones del menú ----------

    /**
     * Opción 1: pide los datos de la sala (capacidad, filas y columnas por separado) y su tipo
     * en un combo. Cine rechaza la sala si el código se repite o capacidad != filas*columnas.
     */
    private static void registrarSala(Cine cine) {
        String codigo = leerTexto("Código de la sala:");
        if (codigo == null) return;
        String nombre = leerTexto("Nombre de la sala:");
        if (nombre == null) return;
        Integer capacidad = leerEntero("Capacidad máxima (> 0):", 1);
        if (capacidad == null) return;
        Integer filas = leerEntero("Número de filas (> 0):", 1);
        if (filas == null) return;
        Integer columnas = leerEntero("Número de columnas (> 0):", 1);
        if (columnas == null) return;
        Object tipo = JOptionPane.showInputDialog(null, "Tipo de sala:", TITULO,
                JOptionPane.QUESTION_MESSAGE, null, TipoSala.values(), TipoSala.values()[0]);
        if (tipo == null) return;
        Sala sala = new Sala(codigo, nombre, capacidad, (TipoSala) tipo, filas, columnas);
        if (cine.registrarSala(sala)) {
            mostrar("Sala registrada:\n" + sala);
        } else {
            mostrar("No se registró la sala. Causa probable: código duplicado o "
                    + "la capacidad (" + capacidad + ") no es igual a filas x columnas ("
                    + (filas * columnas) + ").");
        }
    }

    /** Opción 2: pide los datos de la película (género en combo) y la registra. */
    private static void registrarPelicula(Cine cine) {
        String codigo = leerTexto("Código de la película:");
        if (codigo == null) return;
        String titulo = leerTexto("Título:");
        if (titulo == null) return;
        Integer duracion = leerEntero("Duración en minutos (> 0):", 1);
        if (duracion == null) return;
        Object genero = JOptionPane.showInputDialog(null, "Género:", TITULO,
                JOptionPane.QUESTION_MESSAGE, null, Genero.values(), Genero.values()[0]);
        if (genero == null) return;
        Integer edad = leerEntero("Clasificación de edad (>= 0):", 0);
        if (edad == null) return;
        Pelicula pelicula = new Pelicula(codigo, titulo, duracion, (Genero) genero, edad);
        if (cine.registrarPelicula(pelicula)) {
            mostrar("Película registrada:\n" + pelicula);
        } else {
            mostrar("No se registró la película. Causa probable: código duplicado.");
        }
    }

    /** Opción 3: pide los datos del cliente (descuento entre 0 y 100) y lo registra. */
    private static void registrarCliente(Cine cine) {
        String documento = leerTexto("Documento:");
        if (documento == null) return;
        String nombre = leerTexto("Nombre completo:");
        if (nombre == null) return;
        String correo = leerTexto("Correo:");
        if (correo == null) return;
        String telefono = leerTexto("Teléfono:");
        if (telefono == null) return;
        String afiliacion = leerTexto("Código de afiliación:");
        if (afiliacion == null) return;
        Double descuento = leerDouble("Porcentaje de descuento (0 a 100):", 0, 100);
        if (descuento == null) return;
        Cliente cliente = new Cliente(documento, nombre, correo, telefono, afiliacion, descuento);
        if (cine.registrarCliente(cliente)) {
            mostrar("Cliente registrado:\n" + cliente);
        } else {
            mostrar("No se registró el cliente. Causa probable: el documento ya existe "
                    + "como cliente u operario.");
        }
    }

    /** Opción 4: pide los datos del operario (fecha de contratación y salario >= 0) y lo registra. */
    private static void registrarOperario(Cine cine) {
        String documento = leerTexto("Documento:");
        if (documento == null) return;
        String nombre = leerTexto("Nombre completo:");
        if (nombre == null) return;
        String correo = leerTexto("Correo:");
        if (correo == null) return;
        String telefono = leerTexto("Teléfono:");
        if (telefono == null) return;
        String empleado = leerTexto("Código de empleado:");
        if (empleado == null) return;
        LocalDate contratacion = leerFecha("Fecha de contratación (yyyy-MM-dd):");
        if (contratacion == null) return;
        Double salario = leerDouble("Salario básico (>= 0):", 0, Double.MAX_VALUE);
        if (salario == null) return;
        Operario operario = new Operario(documento, nombre, correo, telefono, empleado, contratacion, salario);
        if (cine.registrarOperario(operario)) {
            mostrar("Operario registrado:\n" + operario);
        } else {
            mostrar("No se registró el operario. Causa probable: el documento ya existe "
                    + "como cliente u operario.");
        }
    }

    /**
     * Opción 5: pide los datos de la función. Antes de crearla verifica que la película
     * y la sala existan, avisando cuál falta. Cine rechaza códigos repetidos y traslapes.
     */
    private static void programarFuncion(Cine cine) {
        String codigo = leerTexto("Código de la función:");
        if (codigo == null) return;
        String codPelicula = leerTexto("Código de la película:");
        if (codPelicula == null) return;
        Pelicula pelicula = cine.buscarPelicula(codPelicula);
        if (pelicula == null) {
            mostrar("No existe una película con código " + codPelicula + ". Regístrela primero.");
            return;
        }
        String codSala = leerTexto("Código de la sala:");
        if (codSala == null) return;
        Sala sala = cine.buscarSala(codSala);
        if (sala == null) {
            mostrar("No existe una sala con código " + codSala + ". Regístrela primero.");
            return;
        }
        LocalDate fecha = leerFecha("Fecha de la función (yyyy-MM-dd):");
        if (fecha == null) return;
        LocalTime hora = leerHora("Hora de inicio (HH:mm):");
        if (hora == null) return;
        Double precio = leerDouble("Precio base (>= 0):", 0, Double.MAX_VALUE);
        if (precio == null) return;
        Funcion funcion = new Funcion(codigo, fecha, hora, precio, pelicula, sala);
        if (cine.programarFuncion(funcion)) {
            mostrar("Función programada:\n" + funcion);
        } else {
            mostrar("No se programó la función. Causa probable: código duplicado o "
                    + "conflicto de horario en la sala " + sala.getNombre() + ".");
        }
    }

    /**
     * Opción 6: flujo completo de venta. Primero pide y valida el código de venta, el cliente,
     * el operario y la función (abortando con un mensaje específico si algo falta o se repite).
     * Luego muestra la ocupación actual y pide los asientos como "fila-asiento" separados por
     * coma (ej: 3-5,3-6). Cada asiento se valida por formato, rango, ocupado y repetido, y la
     * pregunta se repite con el error concreto hasta que todos sean válidos. Finalmente llama a
     * Cine.realizarVenta y muestra el resumen con las boletas y el total.
     */
    private static void realizarVenta(Cine cine) {
        String codigo = leerTexto("Código de la venta:");
        if (codigo == null) return;
        if (cine.buscarVenta(codigo) != null) {
            mostrar("Ya existe una venta con código " + codigo + ".");
            return;
        }
        LocalDate fecha = leerFecha("Fecha de la venta (yyyy-MM-dd):");
        if (fecha == null) return;
        String docCliente = leerTexto("Documento del cliente:");
        if (docCliente == null) return;
        if (cine.buscarCliente(docCliente) == null) {
            mostrar("No existe un cliente con documento " + docCliente + ".");
            return;
        }
        String docOperario = leerTexto("Documento del operario:");
        if (docOperario == null) return;
        if (cine.buscarOperario(docOperario) == null) {
            mostrar("No existe un operario con documento " + docOperario + ".");
            return;
        }
        String codFuncion = leerTexto("Código de la función:");
        if (codFuncion == null) return;
        Funcion funcion = cine.buscarFuncion(codFuncion);
        if (funcion == null) {
            mostrar("No existe una función con código " + codFuncion + ".");
            return;
        }
        Sala sala = funcion.getSala();
        String info = "Ocupación actual de " + funcion.getCodigo() + ": "
                + String.format("%.2f%%", funcion.calcularOcupacion()) + " ("
                + funcion.contarBoletasVendidas() + " de " + sala.getCapacidadMaxima() + ")\n"
                + "Filas: 1 a " + sala.getFilas() + ", asientos: 1 a " + sala.getColumnas() + "\n";

        // Se repite la pregunta hasta que todos los asientos sean válidos
        String error = "";
        int[] filas = null;
        int[] asientos = null;
        while (filas == null) {
            String texto = leerTexto(error + info + "Asientos como fila-asiento separados por coma (ej: 3-5,3-6):");
            if (texto == null) return;
            String[] partes = texto.split(",");
            int[] f = new int[partes.length];
            int[] a = new int[partes.length];
            error = "";
            for (int i = 0; i < partes.length && error.isEmpty(); i++) {
                String parte = partes[i].trim();
                String[] par = parte.split("-");
                if (par.length != 2) {
                    error = "Formato inválido en \"" + parte + "\". Use fila-asiento.\n\n";
                    break;
                }
                try {
                    f[i] = Integer.parseInt(par[0].trim());
                    a[i] = Integer.parseInt(par[1].trim());
                } catch (NumberFormatException e) {
                    error = "Formato inválido en \"" + parte + "\": fila y asiento deben ser números.\n\n";
                    break;
                }
                if (f[i] < 1 || f[i] > sala.getFilas() || a[i] < 1 || a[i] > sala.getColumnas()) {
                    error = "El asiento " + f[i] + "-" + a[i] + " está fuera de rango.\n\n";
                } else if (funcion.estaOcupado(f[i], a[i])) {
                    error = "El asiento " + f[i] + "-" + a[i] + " ya está ocupado.\n\n";
                } else {
                    for (int j = 0; j < i; j++) {
                        if (f[j] == f[i] && a[j] == a[i]) {
                            error = "El asiento " + f[i] + "-" + a[i] + " está repetido en la solicitud.\n\n";
                        }
                    }
                }
            }
            if (error.isEmpty()) {
                filas = f;
                asientos = a;
            }
        }

        Venta venta = cine.realizarVenta(codigo, fecha, docCliente, docOperario, codFuncion, filas, asientos);
        if (venta == null) {
            mostrar("No se pudo realizar la venta. Revise los datos e intente de nuevo.");
            return;
        }
        StringBuilder resumen = new StringBuilder();
        resumen.append("VENTA REALIZADA\n");
        resumen.append("Código: ").append(venta.getCodigo()).append("\n");
        resumen.append("Cliente: ").append(venta.getCliente().getNombreCompleto()).append("\n");
        resumen.append("Función: ").append(funcion.getCodigo()).append(" - ")
                .append(funcion.getPelicula().getTitulo()).append(" (")
                .append(funcion.getFecha()).append(" ").append(funcion.getHoraInicio()).append(")\n");
        resumen.append("Boletas:\n");
        for (Boleta boleta : venta.getBoletas()) {
            resumen.append("  ").append(boleta.getCodigo()).append(" - fila ").append(boleta.getFila())
                    .append(", asiento ").append(boleta.getAsiento()).append(": ")
                    .append(String.format("$%,.0f", boleta.getValor())).append("\n");
        }
        resumen.append("TOTAL: ").append(String.format("$%,.0f", venta.calcularTotal()));
        mostrar(resumen.toString());
    }

    /** Opción 7: pide el código de una venta y muestra su total (o avisa si no existe). */
    private static void calcularTotalVenta(Cine cine) {
        String codigo = leerTexto("Código de la venta:");
        if (codigo == null) return;
        double total = cine.calcularTotalVenta(codigo);
        if (total < 0) {
            mostrar("No existe una venta con código " + codigo + ".");
        } else {
            mostrar("Total de la venta " + codigo + ": " + String.format("$%,.0f", total));
        }
    }

    /** Opción 8: pide el código de una función y muestra su ocupación en porcentaje. */
    private static void calcularOcupacion(Cine cine) {
        String codigo = leerTexto("Código de la función:");
        if (codigo == null) return;
        double ocupacion = cine.calcularOcupacionFuncion(codigo);
        if (ocupacion < 0) {
            mostrar("No existe una función con código " + codigo + ".");
        } else {
            mostrar("Ocupación de la función " + codigo + ": " + String.format("%.2f%%", ocupacion));
        }
    }

    /** Opción 9: muestra en una sola ventana las películas, funciones y salas registradas. */
    private static void listar(Cine cine) {
        StringBuilder sb = new StringBuilder();
        sb.append("PELÍCULAS\n");
        if (cine.getPeliculas().isEmpty()) sb.append("  (ninguna)\n");
        for (Pelicula p : cine.getPeliculas()) sb.append("  ").append(p).append("\n");
        sb.append("\nFUNCIONES\n");
        if (cine.getFunciones().isEmpty()) sb.append("  (ninguna)\n");
        for (Funcion f : cine.getFunciones()) sb.append("  ").append(f).append("\n");
        sb.append("\nSALAS\n");
        if (cine.getSalas().isEmpty()) sb.append("  (ninguna)\n");
        for (Sala s : cine.getSalas()) sb.append("  ").append(s).append("\n");
        mostrar(sb.toString());
    }

    /**
     * Opción 10: carga 3 salas, 3 películas, 2 clientes, 1 operario y 4 funciones de ejemplo.
     * Si ya existe S1, P1, el cliente 1001, el operario 2001 o F1, avisa y no carga nada.
     */
    private static void cargarDatosEjemplo(Cine cine) {
        if (cine.buscarSala("S1") != null || cine.buscarPelicula("P1") != null
                || cine.buscarCliente("1001") != null || cine.buscarOperario("2001") != null
                || cine.buscarFuncion("F1") != null) {
            mostrar("Ya existen datos de ejemplo (S1, P1, cliente 1001, operario 2001 o F1). No se cargó nada.");
            return;
        }
        Sala s1 = new Sala("S1", "Sala 1", 80, TipoSala.TRADICIONAL, 8, 10);
        Sala s2 = new Sala("S2", "Sala VIP", 40, TipoSala.VIP, 5, 8);
        Sala s3 = new Sala("S3", "Sala 3D", 60, TipoSala.SALA_3D, 6, 10);
        cine.registrarSala(s1);
        cine.registrarSala(s2);
        cine.registrarSala(s3);

        Pelicula p1 = new Pelicula("P1", "Avengers: Doomsday", 150, Genero.ACCION, 12);
        Pelicula p2 = new Pelicula("P2", "Toy Story 5", 100, Genero.ANIMACION, 0);
        Pelicula p3 = new Pelicula("P3", "El Conjuro 4", 110, Genero.TERROR, 15);
        cine.registrarPelicula(p1);
        cine.registrarPelicula(p2);
        cine.registrarPelicula(p3);

        cine.registrarCliente(new Cliente("1001", "Ana Gómez", "ana.gomez@ejemplo.com",
                "3001112233", "AF-001", 10));
        cine.registrarCliente(new Cliente("1002", "Luis Pérez", "luis.perez@ejemplo.com",
                "3004445566", "AF-002", 0));
        cine.registrarOperario(new Operario("2001", "Carlos Ruiz", "carlos.ruiz@cineplus.com",
                "3007778899", "EMP-01", LocalDate.of(2024, 3, 1), 1800000));

        LocalDate dia = LocalDate.of(2026, 10, 20);
        cine.programarFuncion(new Funcion("F1", dia, LocalTime.of(14, 0), 15000, p1, s1));
        cine.programarFuncion(new Funcion("F2", dia, LocalTime.of(16, 0), 20000, p2, s2));
        cine.programarFuncion(new Funcion("F3", dia, LocalTime.of(19, 0), 18000, p3, s3));
        cine.programarFuncion(new Funcion("F4", dia, LocalTime.of(18, 0), 15000, p2, s1));
        mostrar("Datos de ejemplo cargados: 3 salas, 3 películas, 2 clientes, 1 operario y 4 funciones.");
    }

    // ---------- Auxiliares de entrada (devuelven null si el usuario cancela) ----------

    /** Muestra un mensaje informativo en una ventana. */
    private static void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Muestra un mensaje de error en una ventana. */
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Pide un texto no vacío (se recortan los espacios). Si el usuario deja el campo vacío
     * muestra el error y repite la pregunta; si cancela devuelve null.
     */
    static String leerTexto(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(null, mensaje, TITULO, JOptionPane.QUESTION_MESSAGE);
            if (texto == null) {
                return null;
            }
            texto = texto.trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrarError("El valor no puede estar vacío.");
        }
    }

    /**
     * Pide un entero mayor o igual que minimo. Atrapa NumberFormatException, muestra el error
     * y repite la pregunta; devuelve null si el usuario cancela.
     */
    static Integer leerEntero(String mensaje, int minimo) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto == null) {
                return null;
            }
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo) {
                    return valor;
                }
                mostrarError("El valor debe ser mayor o igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero.");
            }
        }
    }

    /**
     * Pide un número decimal dentro del rango [minimo, maximo]. Atrapa NumberFormatException
     * y repite la pregunta ante errores; devuelve null si el usuario cancela.
     */
    static Double leerDouble(String mensaje, double minimo, double maximo) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto == null) {
                return null;
            }
            try {
                double valor = Double.parseDouble(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                mostrarError("El valor está fuera del rango permitido.");
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
            }
        }
    }

    /**
     * Pide una fecha en formato yyyy-MM-dd. Atrapa DateTimeParseException y repite la
     * pregunta ante errores; devuelve null si el usuario cancela.
     */
    static LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto == null) {
                return null;
            }
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Fecha inválida. Use el formato yyyy-MM-dd.");
            }
        }
    }

    /**
     * Pide una hora en formato HH:mm. Atrapa DateTimeParseException y repite la
     * pregunta ante errores; devuelve null si el usuario cancela.
     */
    static LocalTime leerHora(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto == null) {
                return null;
            }
            try {
                return LocalTime.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarError("Hora inválida. Use el formato HH:mm.");
            }
        }
    }
}
