package scr.model;
import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;
/**
 * Cuenta que cobra una comisión fija por cada retiro.
 */
public class CuentaCorriente extends CuentaBancaria {
    
    private static final double COMISION_RETIRO = 5.0;
    /**
     * Constructor de CuentaCorriente.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de cuenta.
     */
    public CuentaCorriente(String titular, String numeroCuenta) {
        super(titular, numeroCuenta);
    }

    /**
     * Retira el monto solicitado más una comisión fija.
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Si el saldo es insuficiente para cubrir monto + comisión.
     */
    @Override
    public void retirar(double monto) throws OperacionInvalidaException {
        double montoTotal = monto + COMISION_RETIRO;
        
        try {
            // Se usa la lógica de validación de la clase base
            super.retirar(montoTotal);
        } catch (SaldoInsuficienteException e) {
            // Implementación de Excepción Encadenada (Control de Error en Cascada)
            String mensaje = "Fallo en el retiro. Saldo requerido para " + monto + " más comisión de " + COMISION_RETIRO + " es insuficiente.";
            throw new OperacionInvalidaException(mensaje, e);
        }
    }

    /**
     * Implementación de calcularIntereses: la cuenta corriente no genera intereses,
     * pero sí podría cobrar un mantenimiento, que aquí simulamos como una deducción.
     */
    @Override
    public void calcularIntereses() {
        double mantenimiento = 10.0;
        this.saldo -= mantenimiento;
        System.out.println("Cuenta Corriente " + this.numeroCuenta + ": Se cobró $10.0 por mantenimiento. Nuevo saldo: " + this.saldo);
    }
}