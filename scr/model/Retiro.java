package scr.model;

/**
 * Representa un retiro en la cuenta.
 *
 * @author Juan
 * @version 1.0
 */
public class Retiro extends Movimiento {
    /**
     * @param monto Importe del retiro.
     */
    public Retiro(double monto) {
        super(monto);
    }

    /**
     * @param monto Importe del retiro.
     * @param fecha Fecha del retiro.
     */
    public Retiro(double monto, java.time.LocalDateTime fecha) {
        super(monto, fecha);
    }
}
