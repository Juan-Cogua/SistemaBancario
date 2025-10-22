package scr.model;
import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;
/**
 * Cuenta con un límite máximo de retiro por operación.
 *
 * @author Juan
 * @version 1.0
 */
public class CuentaEmpresarial extends CuentaBancaria {
    
    private double limiteRetiro;
    private String gestorAsignado;
    private static final double TASA_INTERES_EMPRESARIAL = 0.001; // 0.1%

    /**
     * Constructor de CuentaEmpresarial con valores por defecto.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de la cuenta.
     */
    public CuentaEmpresarial(String titular, String numeroCuenta) {
        super(titular, numeroCuenta);
        this.limiteRetiro = 5000.0;
        this.gestorAsignado = "";
    }

    /**
     * Constructor parametrizado de CuentaEmpresarial.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de la cuenta.
     * @param limiteRetiro Límite máximo de retiro por operación.
     * @param gestorAsignado Nombre del gestor asignado.
     */
    public CuentaEmpresarial(String titular, String numeroCuenta, double limiteRetiro, String gestorAsignado) {
        super(titular, numeroCuenta);
        this.limiteRetiro = limiteRetiro;
        this.gestorAsignado = gestorAsignado;
    }

    /**
     * Retira un monto, validando si excede el límite de retiro por operación.
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Si el monto excede el límite o el saldo es insuficiente (encadenada).
     */
    /**
     * Retira un monto, validando límites y encadenando excepciones si aplica.
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Si el monto excede el límite o el saldo es insuficiente.
     */
    @Override
    public void retirar(double monto) throws OperacionInvalidaException {
        if (monto > this.limiteRetiro) {
            // Lanza OperacionInvalidaException SIN causa encadenada
            throw new OperacionInvalidaException("El retiro de $" + monto +
                " excede el límite máximo por operación de $" + this.limiteRetiro);
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

    public double getLimiteRetiro() { return limiteRetiro; }
    public void setLimiteRetiro(double limite) { this.limiteRetiro = limite; }
    public String getGestorAsignado() { return gestorAsignado; }
    public void setGestorAsignado(String gestor) { this.gestorAsignado = gestor; }
}