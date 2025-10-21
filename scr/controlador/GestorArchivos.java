package scr.controlador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase responsable de manejar la persistencia de transacciones 
 * en archivos de texto (logs).
 */
public class GestorArchivos {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra un movimiento (depósito o retiro) en el archivo de log correspondiente.
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
}