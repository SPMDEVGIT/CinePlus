/**
 * Tipos de sala que ofrece el cine.
 * El tipo determina el recargo que {@link Sala#calcularRecargo()} aplica al precio base:
 * TRADICIONAL 0%, VIP 30% y SALA_3D 20%.
 */
public enum TipoSala {
    TRADICIONAL, VIP, SALA_3D
}
