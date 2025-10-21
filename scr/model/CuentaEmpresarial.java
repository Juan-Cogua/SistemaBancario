package scr.model;
import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;
/**
 * Cuenta con un límite máximo de retiro por operación.
 */
public class CuentaEmpresarial extends CuentaBancaria {
    
    private static final double LIMITE_RETIRO_POR_OPERACION = 5000.0;
    private static final double TASA_INTERES_EMPRESARIAL = 0.001; // 0.1%

    /**
     * Constructor de CuentaEmpresarial.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de cuenta.
     */
    public CuentaEmpresarial(String titular, String numeroCuenta) {
        super(titular, numeroCuenta);
    }

    /**
     * Retira un monto, validando si excede el límite de retiro por operación.
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Si el monto excede el límite o el saldo es insuficiente (encadenada).
     */
    @Override
    public void retirar(double monto) throws OperacionInvalidaException {
        if (monto > LIMITE_RETIRO_POR_OPERACION) {
            // Lanza OperacionInvalidaException SIN causa encadenada
            throw new OperacionInvalidaException("El retiro de $" + monto + 
                " excede el límite máximo por operación de $" + LIMITE_RETIRO_POR_OPERACION);
        }

        try {
            // Llama al método base, que lanzará SaldoInsuficienteException si es necesario
            super.retirar(monto);
        } catch (SaldoInsuficienteException e) {
            // Lanza OperacionInvalidaException CON causa encadenada
            String mensaje = "El retiro es válido por monto, pero el saldo es insuficiente.";
            throw new OperacionInvalidaException(mensaje, e);
        }
    }
    /**
     * Implementación de calcularIntereses: genera un interés bajo.
     */
    @Override
    public void calcularIntereses() {
        double interesesGanados = this.saldo * TASA_INTERES_EMPRESARIAL;
        this.saldo += interesesGanados;
        System.out.printf("Cuenta Empresarial %s: Se generaron $%.2f de intereses. Nuevo saldo: $%.2f\n", 
        this.numeroCuenta, interesesGanados, this.saldo);
    }
}