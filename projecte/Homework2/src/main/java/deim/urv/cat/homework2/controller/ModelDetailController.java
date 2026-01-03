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
 * - Cualquiera puede ver los modelos públicos (info básica)
 * - Los usuarios autenticados ven información adicional (versión, fecha actualización)
 * - Si un modelo es privado y no estás logueado, te redirige al login
 *   y después del login te devuelve automáticamente al modelo
 * 
 * NOTA: Este controlador combina la funcionalidad de vista pública y privada
 * en un solo endpoint. La vista (detall.jsp) decide qué mostrar según
 * si el usuario está autenticado o no.
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
     * - Si el usuario está autenticado: pasa sus credenciales a la API
     *   y puede ver información privada (versión, fechas)
     * - Si no está autenticado: solo ve información pública
     * - Si el modelo es privado y no está autenticado: redirige al login
     */
    @GET
    @Path("{id}")
    public Response showDetail(@PathParam("id") Long modelId) {
        
        try {
            // Preparamos el header de autenticación
            // Si el usuario está logueado, pasamos sus credenciales
            // Si no, pasamos null (la API devolverá solo info pública)
            String authHeader = userSession.isAuthenticated() 
                    ? userSession.getAuthHeader() 
                    : null;
            
            // Llamamos al servicio para obtener el modelo
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
            
            // Pasamos el modelo a la vista JSP
            models.put("model", model);
            
            // Pasamos el estado de autenticación para que la vista sepa
            // si mostrar la sección privada o no
            models.put("authenticated", userSession.isAuthenticated());
            
            // Si está autenticado, pasamos el nombre para "Benvingut X!"
            if (userSession.isAuthenticated()) {
                models.put("username", userSession.getUsername());
            }
            
            // Devolvemos la vista de detalle
            return Response.ok("detall.jsp").build();
            
        } catch (UnauthorizedException e) {
            // La API ha devuelto 401: el modelo es privado y no estamos autenticados
            // Guardamos la URL actual para redirigir después del login
            userSession.setReturnUrl("/Homework2/Web/model/" + modelId);
            
            // Redirigimos al formulario de login
            return Response.seeOther(URI.create("/Homework2/Web/login")).build();
            
        } catch (NotFoundException e) {
            // El modelo no existe (404 de la API)
            models.put("errorCode", 404);
            models.put("errorTitle", "Model no trobat");
            models.put("errorMessage", "El model amb ID " + modelId + " no existeix");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("error.jsp")
                    .build();
            
        } catch (Exception e) {
            // Cualquier otro error (red, servidor, etc.)
            models.put("errorCode", 500);
            models.put("errorTitle", "Error del servidor");
            models.put("errorMessage", "Hi ha hagut un problema: " + e.getMessage());
            return Response.serverError()
                    .entity("error.jsp")
                    .build();
        }
    }
}
