package service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;

/**
 * ExceptionMapper para EntityNotFoundException de JPA
 * Se lanza cuando no se encuentra una entidad por ID
 */
@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        // Crear objeto de error
        GenericExceptionMapper.ErrorResponse errorResponse = 
            new GenericExceptionMapper.ErrorResponse(
                404,
                "Not Found",
                exception.getMessage() != null ? exception.getMessage() : "Entity not found"
            );

        // Devolver como JSON con status 404
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
