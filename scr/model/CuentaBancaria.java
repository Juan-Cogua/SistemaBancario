package scr.model;
import scr.excepciones.SaldoInsuficienteException;

/**
 * Clase abstracta base para todos los tipos de cuentas bancarias.
 * Gestiona los atributos comunes y la lógica básica.
 */
public abstract class CuentaBancaria {
    // Atributos protegidos para acceso en subclases
    protected String titular;
    protected double saldo;
    protected String numeroCuenta;

    /**
     * Constructor para inicializar una cuenta.
     * @param titular Nombre del titular de la cuenta.
     * @param numeroCuenta Número único de la cuenta.
     */
    public CuentaBancaria(String titular, String numeroCuenta) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = 0.0;
    }

    /**
     * Realiza un depósito en la cuenta.
     * @param monto Cantidad a depositar. Debe ser positivo.
     */
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
        }
    }

    /**
     * Intenta retirar una cantidad de la cuenta.
     * Este método es base y puede ser redefinido por las subclases.
     * @param monto Cantidad a retirar.
     * @throws SaldoInsuficienteException Si el saldo es menor al monto solicitado.
     */
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > 0) {
            if (this.saldo >= monto) {
                this.saldo -= monto;
            } else {
                throw new SaldoInsuficienteException("El saldo es insuficiente para el retiro de: " + monto);
            }
        }
    }

    /**
     * Método abstracto que obliga a las subclases a implementar su
     * propia lógica de cálculo de intereses o comisiones.
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
}
