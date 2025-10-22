package scr.model;

import scr.excepciones.SaldoInsuficienteException;
import scr.excepciones.OperacionInvalidaException;

/**
 * Cuenta de ahorros que genera intereses mensuales.
 *
 * @author Juan
 * @version 1.0
 */
public class CuentaAhorros extends CuentaBancaria {

    private static final double TASA_INTERES = 0.02; // 2% mensual (ejemplo)

    private double tasaInteresMensual;
    private int cantidadRetirosMes;
    private int retirosRealizadosEsteMes;

    /**
     * Constructor por defecto para CuentaAhorros.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de cuenta.
     */
    public CuentaAhorros(String titular, String numeroCuenta) {
        super(titular, numeroCuenta);
        this.tasaInteresMensual = TASA_INTERES;
        this.cantidadRetirosMes = Integer.MAX_VALUE; // por defecto sin límite
        this.retirosRealizadosEsteMes = 0;
    }

    /**
     * Constructor parametrizado para CuentaAhorros.
     * @param titular Nombre del titular.
     * @param numeroCuenta Número de cuenta.
     * @param tasaInteresMensual Tasa de interés mensual a aplicar.
     * @param cantidadRetirosMes Cantidad de retiros permitidos por mes.
     */
    public CuentaAhorros(String titular, String numeroCuenta, double tasaInteresMensual, int cantidadRetirosMes) {
        super(titular, numeroCuenta);
        this.tasaInteresMensual = tasaInteresMensual;
        this.cantidadRetirosMes = cantidadRetirosMes;
        this.retirosRealizadosEsteMes = 0;
    }

    /**
     * Intenta retirar del ahorro y contabiliza retiros mensuales.
     * @param monto Cantidad a retirar.
     * @throws OperacionInvalidaException Cuando no es posible retirar (encadena causa si aplica).
     */
    @Override
    public void retirar(double monto) throws OperacionInvalidaException {
        try {
            // Si existe un límite de retiros gratuitos, podríamos aplicar comisión o denegar
            // Aquí solo contabilizamos retiros para futuras reglas
            super.retirar(monto);
            this.retirosRealizadosEsteMes++;
        } catch (SaldoInsuficienteException e) {
            throw new OperacionInvalidaException("No se pudo realizar el retiro en CuentaAhorros.", e);
        }
    }

    /**
     * Calcula e incorpora los intereses mensuales al saldo.
     */
    @Override
    public void calcularIntereses() {
        double intereses = this.saldo * this.tasaInteresMensual;
        this.saldo += intereses;
        System.out.printf("Cuenta Ahorros %s: Se generaron $%.2f de intereses. Nuevo saldo: $%.2f\n",
                this.numeroCuenta, intereses, this.saldo);
    }

    public double getTasaInteresMensual() { return tasaInteresMensual; }
    public void setTasaInteresMensual(double tasa) { this.tasaInteresMensual = tasa; }
    public int getCantidadRetirosMes() { return cantidadRetirosMes; }
    public void setCantidadRetirosMes(int cantidad) { this.cantidadRetirosMes = cantidad; }
    public int getRetirosRealizadosEsteMes() { return retirosRealizadosEsteMes; }

}

