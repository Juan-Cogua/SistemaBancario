package scr.model;

import java.time.LocalDateTime;

/**
 * Representa un movimiento genérico en la cuenta (depósito o retiro).
 *
 * @author Juan
 * @version 1.0
 */
public abstract class Movimiento {
    protected double monto;
    protected LocalDateTime fecha;

    /**
     * Crea un movimiento con fecha actual.
     * @param monto Importe del movimiento.
     */
    public Movimiento(double monto) {
        this.monto = monto;
        this.fecha = LocalDateTime.now();
    }

    /**
     * Crea un movimiento con fecha explícita (útil al cargar desde archivo).
     * @param monto Importe del movimiento.
     * @param fecha Fecha y hora del movimiento.
     */
    public Movimiento(double monto, LocalDateTime fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
