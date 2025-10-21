package scr.excepciones;
/**
 * Excepción lanzada cuando una operación viola una regla de negocio
 * específica de la cuenta (ej. límite de retiro).
 * Está diseñada para encadenar otras excepciones como su causa.
 */
public class OperacionInvalidaException extends Exception {
    /**
     * Constructor que permite encadenar una causa raíz (otra excepción).
     * @param mensaje Descripción del error de operación.
     * @param causa Excepción original que desencadenó el error (ej. SaldoInsuficienteException).
     */
    public OperacionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor que solo recibe el mensaje.
     * @param mensaje Descripción del error de operación.
     */
    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
