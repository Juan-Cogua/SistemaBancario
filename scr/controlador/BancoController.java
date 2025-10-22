package scr.controlador;
import scr.excepciones.OperacionInvalidaException;
import scr.excepciones.SaldoInsuficienteException;
import scr.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controlador de alto nivel que maneja la colección de cuentas y las operaciones
 * delegadas desde la clase Main.
 *
 * @author Juan
 * @version 1.0
 */
public class BancoController {

    private List<CuentaBancaria> cuentas;
    private GestorArchivos gestor;

    public BancoController() {
        this.cuentas = new ArrayList<>();
        this.gestor = new GestorArchivos();
    }

    /**
     * Crea una cuenta interactiva solicitando datos por consola.
     *
     * @author Juan
     * @version 1.0
     * @param sc Scanner abierto para lectura de entrada del usuario.
     */
    public void crearCuentaInteractive(Scanner sc) {
        System.out.println("Tipo de cuenta a crear: 1) Corriente 2) Ahorros 3) Empresarial");
        System.out.print("Elija tipo: ");
        int tipo = Integer.parseInt(sc.nextLine());
        System.out.print("Titular: ");
        String titular = sc.nextLine();
        System.out.print("Saldo inicial: ");
        double saldo = Double.parseDouble(sc.nextLine());
        // Generar número de cuenta: 6 dígitos numéricos (con padding) y asegurar unicidad
        String numero;
        do {
            int n = (int)(Math.random() * 1_000_000);
            numero = String.format("%06d", n);
        } while (buscarCuenta(numero) != null);
        CuentaBancaria cuenta = null;

        switch (tipo) {
            case 1:
                System.out.print("Comisión por retiro (por defecto 5.0): ");
                double com = Double.parseDouble(sc.nextLine());
                cuenta = new CuentaCorriente(titular, numero, com);
                break;
            case 2:
                System.out.print("Tasa de interés mensual (ej: 0.02): ");
                double tasa = Double.parseDouble(sc.nextLine());
                System.out.print("Cantidad de retiros gratis por mes (ej: 3): ");
                int cant = Integer.parseInt(sc.nextLine());
                cuenta = new CuentaAhorros(titular, numero, tasa, cant);
                break;
            case 3:
                System.out.print("Límite de retiro por operación (ej: 5000): ");
                double limite = Double.parseDouble(sc.nextLine());
                System.out.print("Gestor asignado (nombre): ");
                String gestorNombre = sc.nextLine();
                cuenta = new CuentaEmpresarial(titular, numero, limite, gestorNombre);
                break;
        }

        if (cuenta != null) {
            cuenta.depositar(saldo);
            // registrar en archivo el depósito inicial
            gestor.registrarMovimiento("DEPOSITO", cuenta.getNumeroCuenta(), saldo, cuenta.getSaldo());
            cuentas.add(cuenta);
            System.out.println("Cuenta creada. Número: " + cuenta.getNumeroCuenta() + " Tipo: " + cuenta.getClass().getSimpleName());
        }
    }

    /**
     * Busca una cuenta en memoria por su número.
     *
     * @author Juan
     * @version 1.0
     * @param numero Número de cuenta a buscar.
     * @return La instancia de {@link scr.model.CuentaBancaria} si se encuentra, o null si no existe.
     */
    public CuentaBancaria buscarCuenta(String numero) {
        for (CuentaBancaria c : cuentas) {
            if (c.getNumeroCuenta().equals(numero)) return c;
        }
        return null;
    }

    /**
     * Solicita por consola el número de cuenta y el monto a retirar y ejecuta la operación.
     *
     * @author Juan
     * @version 1.0
     * @param sc Scanner para lectura del usuario.
     * @throws OperacionInvalidaException cuando la operación viola reglas de la cuenta (delegada internamente).
     */
    public void retirarInteractive(Scanner sc) {
        System.out.print("Número de cuenta: ");
        String num = sc.nextLine();
        CuentaBancaria c = buscarCuenta(num);
        if (c == null) { System.out.println("Cuenta no encontrada."); return; }
        System.out.println("Tipo: " + c.getClass().getSimpleName());
        System.out.print("Monto a retirar: ");
        double monto = Double.parseDouble(sc.nextLine());
        try {
            c.retirar(monto);
            gestor.registrarMovimiento("RETIRO", c.getNumeroCuenta(), monto, c.getSaldo());
            System.out.println("Retiro exitoso. Saldo actual: " + c.getSaldo());
        } catch (OperacionInvalidaException e) {
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) System.err.println("Causa: " + e.getCause().getMessage());
        } catch (SaldoInsuficienteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Solicita por consola el número de cuenta y el monto a depositar y ejecuta la operación.
     *
     * @author Juan
     * @version 1.0
     * @param sc Scanner para lectura del usuario.
     */
    public void depositarInteractive(Scanner sc) {
        System.out.print("Número de cuenta: ");
        String num = sc.nextLine();
        CuentaBancaria c = buscarCuenta(num);
        if (c == null) { System.out.println("Cuenta no encontrada."); return; }
        System.out.println("Tipo: " + c.getClass().getSimpleName());
        System.out.print("Monto a depositar: ");
        double monto = Double.parseDouble(sc.nextLine());
        c.depositar(monto);
        gestor.registrarMovimiento("DEPOSITO", c.getNumeroCuenta(), monto, c.getSaldo());
        System.out.println("Depósito exitoso. Saldo actual: " + c.getSaldo());
    }

    /**
     * Muestra los movimientos (depósitos y retiros) de una cuenta. Si la cuenta no
     * existe en memoria, intenta leer los movimientos desde los archivos de log.
     *
     * @author Juan
     * @version 1.0
     * @param sc Scanner para lectura del usuario.
     */
    public void mostrarMovimientosInteractive(Scanner sc) {
        System.out.print("Número de cuenta: ");
        String num = sc.nextLine();
        CuentaBancaria c = buscarCuenta(num);
        if (c == null) {
            // Intentar cargar movimientos desde archivo incluso si la cuenta no está en memoria
            List<Deposito> depsEnArchivo = gestor.cargarDepositosParaCuenta(num);
            List<Retiro> rtsEnArchivo = gestor.cargarRetirosParaCuenta(num);
            if ((depsEnArchivo == null || depsEnArchivo.isEmpty()) && (rtsEnArchivo == null || rtsEnArchivo.isEmpty())) {
                System.out.println("Cuenta no encontrada en memoria ni en archivos.");
                return;
            }

            System.out.println("--- Depósitos---");
            for (Deposito d : depsEnArchivo) {
                System.out.println(d.getFecha() + " - $" + d.getMonto());
            }
            System.out.println("--- Retiros---");
            for (Retiro r : rtsEnArchivo) {
                System.out.println(r.getFecha() + " - $" + r.getMonto());
            }
            return;
        }

        List<Deposito> depsEnArchivo = gestor.cargarDepositosParaCuenta(num);
        for (Deposito d : depsEnArchivo) {
            boolean existe = false;
            for (Deposito ex : c.getDepositos()) {
                if (Double.compare(ex.getMonto(), d.getMonto()) == 0 && ex.getFecha().equals(d.getFecha())) { existe = true; break; }
            }
            if (!existe) c.getDepositos().add(d);
        }

        List<Retiro> rtsEnArchivo = gestor.cargarRetirosParaCuenta(num);
        for (Retiro r : rtsEnArchivo) {
            boolean existe = false;
            for (Retiro ex : c.getRetiros()) {
                if (Double.compare(ex.getMonto(), r.getMonto()) == 0 && ex.getFecha().equals(r.getFecha())) { existe = true; break; }
            }
            if (!existe) c.getRetiros().add(r);
        }

        System.out.println("--- Depósitos ---");
        for (Deposito d : c.getDepositos()) {
            System.out.println(d.getFecha() + " - $" + d.getMonto());
        }
        System.out.println("--- Retiros ---");
        for (Retiro r : c.getRetiros()) {
            System.out.println(r.getFecha() + " - $" + r.getMonto());
        }
    }

    /**
     * Devuelve la lista de cuentas (útil para integraciones o pruebas).
     */
    /**
     * Devuelve la lista de cuentas en memoria (útil para integraciones o pruebas).
     *
     * @author Juan
     * @version 1.0
     * @return Lista de cuentas almacenadas en memoria.
     */
    public List<CuentaBancaria> getCuentas() {
        return cuentas;
    }
}
