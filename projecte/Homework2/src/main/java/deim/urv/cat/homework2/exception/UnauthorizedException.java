package deim.urv.cat.homework2.exception;

/**
 * Excepción para cuando la API devuelve un error 401 (Unauthorized).
 * Esto significa que el usuario no está autenticado o las credenciales son incorrectas.
 * 
 * La lanzamos cuando intentamos acceder a un recurso protegido sin login
 * o cuando el usuario/contraseña no son válidos.
 */
public class UnauthorizedException extends RestClientException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
