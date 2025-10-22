package scr.model;
import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;
/**
 * Cuenta que cobra una comisión fija por cada retiro.
 *
 * @author Juan
 * @version 1.0
 */
public class CuentaCorriente extends CuentaBancaria {
    
    // Comisión por retiro específica de esta cuenta
    private double comisionRetiro;
    /**
     * Constructor de CuentaCorriente.
     *
     * @author Juan
     * @version 1.0
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de cuenta.
     */
    public CuentaCorriente(String titular, String numeroCuenta) {
        super(titular, numeroCuenta);
        this.comisionRetiro = 5.0; // valor por defecto si no se especifica
    }

    public CuentaCorriente(String titular, String numeroCuenta, double comisionRetiro) {
        super(titular, numeroCuenta);
        this.comisionRetiro = comisionRetiro;
    }

    /**
     * Retira el monto solicitado más una comisión fija.
     *
     * @author Juan
     * @version 1.0
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Si el saldo es insuficiente para cubrir monto + comisión.
     */
    @Override
    public void retirar(double monto) throws OperacionInvalidaException {
    double montoTotal = monto + this.comisionRetiro;
        
        try {
            // Se usa la lógica de validación de la clase base
            super.retirar(montoTotal);
        } catch (SaldoInsuficienteException e) {
            // Implementación de Excepción Encadenada (Control de Error en Cascada)
            String mensaje = "Fallo en el retiro. Saldo requerido para " + monto + " más comisión de " + this.comisionRetiro + " es insuficiente.";
            throw new OperacionInvalidaException(mensaje, e);
        }
    }

    /**
     * Implementación de calcularIntereses: la cuenta corriente no genera intereses,
     * pero sí podría cobrar un mantenimiento, que aquí simulamos como una deducción.
     *
     * @author Juan
     * @version 1.0
     */
    @Override
    public void calcularIntereses() {
        double mantenimiento = 10.0;
        this.saldo -= mantenimiento;
        System.out.println("Cuenta Corriente " + this.numeroCuenta + ": Se cobró $" + mantenimiento + " por mantenimiento. Nuevo saldo: " + this.saldo);
    }

    public double getComisionRetiro() {
        return comisionRetiro;
    }
    public void setComisionRetiro(double comisionRetiro) {
        this.comisionRetiro = comisionRetiro;
    }
}