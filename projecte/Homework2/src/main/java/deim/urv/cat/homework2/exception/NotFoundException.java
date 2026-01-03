package deim.urv.cat.homework2.exception;

/**
 * Excepción para cuando la API devuelve un error 404 (Not Found).
 * Esto significa que el recurso que buscamos no existe.
 * 
 * Por ejemplo: si pedimos el modelo con ID 9999 y no existe,
 * la API nos devuelve 404 y nosotros lanzamos esta excepción.
 */
public class NotFoundException extends RestClientException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
