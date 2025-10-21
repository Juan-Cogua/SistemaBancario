import scr.controlador.GestorArchivos;
import scr.excepciones.OperacionInvalidaException;
import scr.excepciones.SaldoInsuficienteException;
import scr.model.*;

/**
 * Clase principal para simular el sistema bancario.
 * Actúa como un controlador simple que coordina las operaciones 
 * entre el Modelo (Cuentas) y la Persistencia (GestorArchivos).
 */
public class main {

    public static void main(String[] args) {
        
        // Inicialización del gestor de persistencia
        GestorArchivos gestor = new GestorArchivos();

        // Inicialización de cuentas (Modelo)
        CuentaCorriente cc = new CuentaCorriente("Ana Garcia", "CC-1001");
        CuentaAhorros ca = new CuentaAhorros("Luis Perez", "CA-2002");
        CuentaEmpresarial ce = new CuentaEmpresarial("Tech Corp", "CE-3003");

        System.out.println("--- INICIO DE SIMULACIÓN BANCARIA ---");
        
        // 1. DEPÓSITOS INICIALES (Funciones básicas)
        System.out.println("\n--- 1. Depósitos ---");
        depositar(cc, 1500.0, gestor);
        depositar(ca, 500.0, gestor);
        depositar(ce, 10000.0, gestor);

        // 2. PRUEBA DE POLIMORFISMO Y LÓGICA DE INTERESES/COMISIONES
        System.out.println("\n--- 2. Cálculo de Intereses/Mantenimiento (Polimorfismo) ---");
        cc.calcularIntereses();
        ca.calcularIntereses();
        ce.calcularIntereses();
        
        // 3. RETIROS BÁSICOS Y PRUEBAS DE EXCEPCIONES ENCADENADAS
        System.out.println("\n--- 3. Pruebas de Retiros y Excepciones ---");
        
        // Prueba 3.1: Retiro exitoso en Cuenta de Ahorros
        retirar(ca, 100.0, gestor);

        // Prueba 3.2: Retiro en Cuenta Corriente (Cobra comisión de 5.0)
        retirar(cc, 500.0, gestor);
        
        // Prueba 3.3: Excepción Encadenada (Cuenta Corriente)
        // Intentamos retirar una cantidad que dejaría saldo insuficiente incluyendo la comisión
        retirar(cc, 1500.0, gestor); 

        // Prueba 3.4: Excepción por límite (Cuenta Empresarial)
        // El monto viola la regla de negocio (límite de 5000.0)
        retirar(ce, 6000.0, gestor);
        
        // Prueba 3.5: Excepción Encadenada (Cuenta Empresarial - Saldo Insuficiente)
        // Si no viola el límite, pero no hay saldo
        retirar(ce, 15000.0, gestor); // Esto fallará por saldo después de los otros retiros

        System.out.println("\n--- FIN DE SIMULACIÓN BANCARIA ---");
        System.out.println("Verifique los archivos 'depositos.txt' y 'retiros.txt' para el log de movimientos.");
    }
    
    // Métodos utilitarios para simplificar la lógica del controlador
    
    private static void depositar(CuentaBancaria cuenta, double monto, GestorArchivos gestor) {
        System.out.printf("Intentando depositar $%.2f en %s (%s)... ", monto, cuenta.getClass().getSimpleName(), cuenta.getNumeroCuenta());
        cuenta.depositar(monto);
        gestor.registrarMovimiento("DEPOSITO", cuenta.getNumeroCuenta(), monto, cuenta.getSaldo());
        System.out.printf("OK. Saldo actual: $%.2f\n", cuenta.getSaldo());
    }
    
    private static void retirar(CuentaBancaria cuenta, double monto, GestorArchivos gestor) {
        System.out.printf("\nIntentando retirar $%.2f de %s (%s)... ", monto, cuenta.getClass().getSimpleName(), cuenta.getNumeroCuenta());
        try {
            // Llama a la lógica de negocio (Polimórfico)
            cuenta.retirar(monto);
            
            // Si tiene éxito, registra el movimiento
            gestor.registrarMovimiento("RETIRO", cuenta.getNumeroCuenta(), monto, cuenta.getSaldo());
            System.out.printf("OK. Retiro exitoso. Saldo actual: $%.2f\n", cuenta.getSaldo());
        } catch (OperacionInvalidaException e) {
            // Captura la OperacionInvalida (puede contener una SaldoInsuficiente anidada)
            System.err.println("FALLÓ. Error: " + e.getMessage());
            if (e.getCause() != null) {
                // Muestra la causa original (Excepción Encadenada)
                System.err.println(" -> Causa Raíz (Encadenada): " + e.getCause().getMessage());
            }
        } catch (SaldoInsuficienteException e) {
             // Captura la SaldoInsuficiente (Solo si es lanzada directamente por CuentaBancaria/Ahorros)
            System.err.println("FALLÓ. Error: " + e.getMessage());
        }
    }
}