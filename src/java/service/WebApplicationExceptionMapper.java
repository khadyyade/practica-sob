package service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;

/**
 * ExceptionMapper para WebApplicationException (404, 400, etc.)
 * Tiene prioridad sobre el GenericExceptionMapper
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        String message = exception.getMessage();

        // Si el mensaje está vacío, usar uno por defecto según el código
        if (message == null || message.isEmpty()) {
            message = getDefaultMessage(status);
        }

        // Crear objeto de error
        GenericExceptionMapper.ErrorResponse errorResponse = 
            new GenericExceptionMapper.ErrorResponse(
                status,
                Response.Status.fromStatusCode(status).getReasonPhrase(),
                message
            );

        // Devolver como JSON
        return Response
                .status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String getDefaultMessage(int status) {
        switch (status) {
            case 400:
                return "Bad Request - Invalid input";
            case 401:
                return "Unauthorized - Authentication required";
            case 403:
                return "Forbidden - Access denied";
            case 404:
                return "Not Found - Resource does not exist";
            case 405:
                return "Method Not Allowed";
            case 500:
                return "Internal Server Error";
            default:
                return "Error processing request";
        }
    }
}
