package service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;

/**
 * ExceptionMapper global para capturar todas las excepciones
 * y devolverlas en formato JSON/XML en lugar de HTML
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        // Crear objeto de error
        ErrorResponse errorResponse = new ErrorResponse(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            exception.getClass().getSimpleName(),
            exception.getMessage() != null ? exception.getMessage() : "Internal Server Error"
        );

        // Devolver como JSON
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Clase interna para representar errores
     */
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;

        public ErrorResponse() {
        }

        public ErrorResponse(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
