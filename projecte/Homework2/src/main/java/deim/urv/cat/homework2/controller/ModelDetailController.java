package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.exception.NotFoundException;
import deim.urv.cat.homework2.exception.UnauthorizedException;
import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.model.UserSession;
import deim.urv.cat.homework2.service.ModelService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.net.URI;

/**
 * Controlador para mostrar el detalle de un modelo de IA.
 * 
 * Según el enunciado:
 * - Cualquiera puede ver los modelos públicos
 * - Solo los usuarios autenticados pueden ver los modelos privados
 * - Si intentas ver un modelo privado sin login, te manda al formulario
 *   y después del login te devuelve automáticamente al modelo
 */
@Controller
@Path("/model")
public class ModelDetailController {

    @Inject
    private Models models;
    
    @Inject
    private ModelService modelService;
    
    @Inject
    private UserSession userSession;

    /**
     * GET /model/{id}
     * Muestra el detalle de un modelo.
     * 
     * Si el modelo es privado y el usuario no está logueado,
     * lo redirigimos al login guardando la URL actual.
     */
    @GET
    @Path("{id}")
    public Response showDetail(@PathParam("id") Long modelId) {
        
        try {
            // Primero intentamos obtener el modelo
            // Si el usuario está autenticado, pasamos sus credenciales
            // Si no, pasamos null y la API nos dirá si podemos verlo o no
            String authHeader = userSession.isAuthenticated() ? userSession.getAuthHeader() : null;
            
            ModelDTO model = modelService.getModelById(modelId, authHeader);
            
            // Si el modelo no existe, mostramos error 404
            if (model == null) {
                models.put("errorCode", 404);
                models.put("errorTitle", "Model no trobat");
                models.put("errorMessage", "El model que busques no existeix");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("error.jsp")
                        .build();
            }
            
            // Pasamos el modelo a la vista
            models.put("model", model);
            
            // También pasamos si el usuario está autenticado (para mostrar "Benvingut X!")
            models.put("authenticated", userSession.isAuthenticated());
            if (userSession.isAuthenticated()) {
                models.put("username", userSession.getUsername());
            }
            
            return Response.ok("detall.jsp").build();
            
        } catch (UnauthorizedException e) {
            // La API ha devuelto 401: el modelo es privado y no estamos autenticados
            // Guardamos a qué página quería ir el usuario para devolverlo después del login
            userSession.setReturnUrl("/Homework2/model/" + modelId);
            
            // Lo mandamos al formulario de login
            return Response.seeOther(URI.create("/Homework2/login")).build();
            
        } catch (NotFoundException e) {
            // El modelo no existe
            models.put("errorCode", 404);
            models.put("errorTitle", "Model no trobat");
            models.put("errorMessage", "El model amb ID " + modelId + " no existeix");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("error.jsp")
                    .build();
            
        } catch (Exception e) {
            // Cualquier otro error
            models.put("errorCode", 500);
            models.put("errorTitle", "Error del servidor");
            models.put("errorMessage", "Hi ha hagut un problema: " + e.getMessage());
            return Response.serverError()
                    .entity("error.jsp")
                    .build();
        }
    }
}
