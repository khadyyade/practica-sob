package deim.urv.cat.homework2.exception;

/**
 * Excepción genérica para errores del cliente REST.
 * La usamos cuando algo falla al conectar con la API de Homework1
 * y no es un error específico como 401 o 404.
 * 
 * Extiende RuntimeException para no tener que declarar "throws" en todos lados,
 * ya que estos errores pueden pasar en cualquier momento y queremos
 * manejarlos de forma centralizada.
 */
public class RestClientException extends RuntimeException {

    // Constructor básico: solo con el mensaje de error
    public RestClientException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa: por si queremos guardar la excepción original
    public RestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
