package scr.model;

/**
 * Representa un depósito en la cuenta.
 *
 * @author Juan
 * @version 1.0
 */
public class Deposito extends Movimiento {
    /**
     * @param monto Importe del depósito.
     */
    public Deposito(double monto) {
        super(monto);
    }

    /**
     * @param monto Importe del depósito.
     * @param fecha Fecha del depósito.
     */
    public Deposito(double monto, java.time.LocalDateTime fecha) {
        super(monto, fecha);
    }
}
