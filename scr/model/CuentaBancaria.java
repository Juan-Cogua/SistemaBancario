package scr.model;

import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta base para todos los tipos de cuentas bancarias.
 * Gestiona los atributos comunes y la lógica básica.
 *
 * @author Juan
 * @version 1.0
 */
public abstract class CuentaBancaria {
    // Atributos protegidos para acceso en subclases
    protected String titular;
    protected double saldo;
    protected String numeroCuenta;

    // Movimientos almacenados en memoria (depósitos y retiros)
    protected List<Deposito> depositos;
    protected List<Retiro> retiros;

    /**
     * Constructor para inicializar una cuenta.
     *
     * @author Juan
     * @version 1.0
     * @param titular Nombre del titular de la cuenta.
     * @param numeroCuenta Número único de la cuenta.
     */
    public CuentaBancaria(String titular, String numeroCuenta) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = 0.0;
        this.depositos = new ArrayList<>();
        this.retiros = new ArrayList<>();
    }

    /**
     * Realiza un depósito en la cuenta y lo registra internamente.
     *
     * @author Juan
     * @version 1.0
     * @param monto Cantidad a depositar. Debe ser positivo.
     */
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
            Deposito d = new Deposito(monto);
            this.depositos.add(d);
        }
    }

    /**
     * Intenta retirar una cantidad de la cuenta.
     * Este método es base y puede ser redefinido por las subclases.
     *
     * @author Juan
     * @version 1.0
     * @param monto Cantidad a retirar.
     * @throws SaldoInsuficienteException Si el saldo es menor al monto solicitado.
     */
    public void retirar(double monto) throws SaldoInsuficienteException, OperacionInvalidaException {
        if (monto > 0) {
            if (this.saldo >= monto) {
                this.saldo -= monto;
                Retiro r = new Retiro(monto);
                this.retiros.add(r);
            } else {
                throw new SaldoInsuficienteException("El saldo es insuficiente para el retiro de: " + monto);
            }
        }
    }

    /**
     * Método abstracto que obliga a las subclases a implementar su
     * propia lógica de cálculo de intereses o comisiones.
     *
     * @author Juan
     * @version 1.0
     */
    public abstract void calcularIntereses();

    // Getters
    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public List<Deposito> getDepositos() {
        return depositos;
    }

    public List<Retiro> getRetiros() {
        return retiros;
    }
}
