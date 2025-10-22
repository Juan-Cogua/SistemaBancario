package scr.controlador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase responsable de manejar la persistencia de transacciones
 * en archivos de texto (logs).
 *
 * @author Juan
 * @version 1.0
 */
public class GestorArchivos {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra un movimiento (depósito o retiro) en el archivo de log correspondiente.
     *
     * @author Juan
     * @version 1.0
     * @param tipoMovimiento "DEPOSITO" o "RETIRO".
     * @param numeroCuenta El número de la cuenta.
     * @param monto Cantidad del movimiento.
     * @param saldoFinal Saldo de la cuenta después del movimiento.
     */
    public void registrarMovimiento(String tipoMovimiento, String numeroCuenta, double monto, double saldoFinal) {
        String archivo;
        
        // Define el nombre del archivo basado en el tipo de movimiento
        if (tipoMovimiento.equals("DEPOSITO")) {
            archivo = "depositos.txt";
        } else if (tipoMovimiento.equals("RETIRO")) {
            archivo = "retiros.txt";
        } else {
            System.err.println("Tipo de movimiento inválido para el registro.");
            return;
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String linea = String.format("%s | Cuenta: %s | Tipo: %s | Monto: $%.2f | Saldo Final: $%.2f",
                                    timestamp, numeroCuenta, tipoMovimiento, monto, saldoFinal);

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log " + archivo + ": " + e.getMessage());
        }
    }

    /**
     * Lee las líneas de un archivo de texto.
     *
     * @author Juan
     * @version 1.0
     * @param archivo Ruta o nombre del archivo a leer.
     * @return Lista de cadenas con las líneas leídas (vacía si hay error o no existe).
     */
    private List<String> leerLineasArchivo(String archivo) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            // Si el archivo no existe o hay otro error devolvemos lista vacía
        }
        return lines;
    }

    /**
     * Parsea las líneas de depósito y devuelve objetos {@link scr.model.Deposito}
     * para la cuenta indicada.
     *
     * @author Juan
     * @version 1.0
     * @param numeroCuenta Número de cuenta a filtrar.
     * @return Lista de depósitos pertenecientes a la cuenta.
     */
    public List<scr.model.Deposito> cargarDepositosParaCuenta(String numeroCuenta) {
        List<scr.model.Deposito> resultado = new ArrayList<>();
        List<String> lines = leerLineasArchivo("depositos.txt");
        for (String l : lines) {
            // Formato esperado: yyyy-MM-dd HH:mm:ss | Cuenta: <num> | Tipo: DEPOSITO | Monto: $<monto> | Saldo Final: $<saldo>
            try {
                String[] parts = l.split("\\s\\|\\s");
                String fechaStr = parts[0].trim();
                String cuentaPart = parts[1].trim();
                String montoPart = parts[3].trim();

                String cuenta = cuentaPart.replace("Cuenta:", "").trim();
                if (!cuenta.equals(numeroCuenta)) continue;

                String montoStr = montoPart.replace("Monto: $", "").trim();
                double monto = Double.parseDouble(montoStr);
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, FORMATTER);
                resultado.add(new scr.model.Deposito(monto, fecha));
            } catch (Exception e) {
            }
        }
        return resultado;
    }

    /**
     * Parsea las líneas de retiros y devuelve objetos {@link scr.model.Retiro}
     * para la cuenta indicada.
     *
     * @author Juan
     * @version 1.0
     * @param numeroCuenta Número de cuenta a filtrar.
     * @return Lista de retiros pertenecientes a la cuenta.
     */
    public List<scr.model.Retiro> cargarRetirosParaCuenta(String numeroCuenta) {
        List<scr.model.Retiro> resultado = new ArrayList<>();
        List<String> lines = leerLineasArchivo("retiros.txt");
        for (String l : lines) {
            try {
                String[] parts = l.split("\\s\\|\\s");
                String fechaStr = parts[0].trim();
                String cuentaPart = parts[1].trim();
                String montoPart = parts[3].trim();

                String cuenta = cuentaPart.replace("Cuenta:", "").trim();
                if (!cuenta.equals(numeroCuenta)) continue;

                String montoStr = montoPart.replace("Monto: $", "").trim();
                double monto = Double.parseDouble(montoStr);
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, FORMATTER);
                resultado.add(new scr.model.Retiro(monto, fecha));
            } catch (Exception e) {
            }
        }
        return resultado;
    }
}