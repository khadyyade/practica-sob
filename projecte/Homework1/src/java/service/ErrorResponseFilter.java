package service;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.time.Instant;

/**
 * Filtro que intercepta TODAS las respuestas y convierte errores HTML a JSON.
 * 
 * Si la respuesta es un error (4xx o 5xx) y no es JSON, la transforma a JSON.
 */
@Provider
@Priority(Priorities.USER)
public class ErrorResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        
        // Solo procesar si es un error (4xx o 5xx)
        if (status >= 400) {
            // Verificar si ya es JSON
            MediaType mediaType = responseContext.getMediaType();
            if (mediaType == null || !mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                // Convertir a JSON
                String errorTitle = getHttpStatusText(status);
                String message = getDefaultMessageForStatus(status);
                
                String jsonError = String.format(
                    "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                    Instant.now().toString(),
                    status,
                    errorTitle,
                    message,
                    requestContext.getUriInfo().getPath()
                );
                
                responseContext.setEntity(jsonError);
                responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
            }
        }
    }
    
    private String getHttpStatusText(int status) {
        switch (status) {
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 409: return "Conflict";
            case 500: return "Internal Server Error";
            case 503: return "Service Unavailable";
            default: return status >= 500 ? "Server Error" : "Client Error";
        }
    }
    
    private String getDefaultMessageForStatus(int status) {
        switch (status) {
            case 400: return "The request could not be understood";
            case 401: return "Authentication is required";
            case 403: return "You don't have permission to access this resource";
            case 404: return "The requested resource was not found";
            case 500: return "An internal server error occurred";
            default: return "An error occurred";
        }
    }
}
