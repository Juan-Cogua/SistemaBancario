package scr.excepciones;
/**
 * Excepción lanzada cuando una operación (retiro) no puede realizarse
 * debido a que el saldo de la cuenta es insuficiente.
 *
 * @author Juan
 * @version 1.0
 */
public class SaldoInsuficienteException extends Exception {
    /**
     * Constructor que recibe el mensaje de error.
     * @param mensaje Descripción del error.
     */
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}